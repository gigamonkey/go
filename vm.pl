#!/usr/bin/env perl -i.bak

#
# Yay no macros and no preprocessor! This script is tightly coupled
# with the structure of VM.java to make it slightly less painful to
# maintain while I figure out exactly what opcodes I want and in what
# order.
#

use strict;
use warnings;

my @lines = ();
my %cases = ();
my $current_case;

my $const_pat    = qr/^(\s+public final static byte ([A-Z][A-Z0-9_]+))\s+=\s+\d+;/;
my $names_start  = qr/^\s+public final static String\[\] NAMES = {/;
my $names_end    = qr/^\s+};/;
my $switch_start = qr/^\s+switch \(op\.opcode\) {/;
my $switch_end   = qr/^\s+default:/;
my $case         = qr/^\s+case ([A-Z][A-Z0-9_]+):/;

# States:
#  - before first opcode: just print lines
#  - collecting opcodes
#  - seen all opcodes: dump them renumbered
#  - before NAMES: print
#  - in NAMES: ignore
#  - end of names: dump names
#  - before switch: print
#  - in switch: collect cases
#  - end of switch: dump all cases (including opcodes not found)
#  - after: print

# states: default, in_opcodes, in_names, in_switch
my $state = 'default';

while (<>) {
    if ($state eq 'default') {
        if (/$const_pat/) {
            push @lines, $_;
            change_state('in_opcodes');
        } elsif (/$switch_start/) {
            print;
            change_state('in_switch');
        } else {
            print;
        }

    } elsif ($state eq 'in_opcodes') {
        if (/$names_start/) {
            dump_constants();
            print;
            change_state('in_names');
        } else {
            push @lines, $_;
        }
    } elsif ($state eq 'in_names') {
        if (/$names_end/) {
            dump_names();
            print;
            change_state('default');
        }
    } elsif ($state eq 'in_switch') {
        if (/$case/) {
            $current_case = $1;
            $cases{$current_case} = [];
        } elsif (/$switch_end/) {
            dump_cases();
            print;
            change_state('default');
        } else {
            push @{$cases{$current_case}}, $_;
        }
    }
}

sub change_state {
    my ($new_state) = @_;
    $state = $new_state;
}

sub dump_constants {
    # Measure
    my $max = 0;
    foreach my $line (@lines) {
        if ($line =~ $const_pat) {
            my $len = length($1);
            $max = $len > $max ? $len : $max;
            #print STDERR "max: $max\n";
        }
    }

    # Output
    my $num = 0;
    foreach my $line (@lines) {
        if ($line =~ $const_pat) {
            print $1;
            print " " x ($max - length($1));
            print " = $num;\n";
            $num++;
        } else {
            print $line;
        }
    }
}

sub dump_names {
    foreach my $line (@lines) {
        if ($line =~ $const_pat) {
            print "        \"$2\",\n";
        }
    }
}

sub dump_cases {
    my %dumped = ();
    foreach my $line (@lines) {
        if ($line =~ $const_pat) {
            $dumped{$2}++;
            print " " x 12;
            print "case $2:\n";
            if (defined $cases{$2}) {
                my @code = @{$cases{$2}};
                if ($#code == 0 and $2 ne 'NOP' and $2 ne 'STOP') {
                    unshift @code, (" " x 16) . "// Implement\n";
                }
                foreach my $line (@code) {
                    print $line;
                }
            } else {
                print " " x 16;
                print "// Implement\n";
                print " " x 16;
                print "break;\n";
            }
        }
    }
    foreach my $case (keys %cases) {
        unless ($dumped{$case}) {
            print " " x 12;
            print "// case $case:\n";
            foreach my $line (@{$cases{$case}}) {
                $line =~ s/^\s+//;
                print " " x 12;
                print "//    $line";
            }
        }
    }
}


__END__
