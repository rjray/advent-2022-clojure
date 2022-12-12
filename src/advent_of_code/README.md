# Breakdown of Files

Here is a breakdown of the various files in this directory. Files with names of the form `dayNN.clj` represent the code actually used to solve the problems (with some tweaking done using a static analysis plug-in for Leinengen). Files with `bis` in the name are modified/tuned versions of the given original day.

The numbers in parentheses in the descriptions of the files represent the rank I had for when my solutions were submitted and accepted.

## day01.clj

Day 1 (117847/112793, approx. 15 min).

Didn't start this one until about 5:30PM on Dec 1st. Good time overall, but high rank-numbers because of this.

## day02.clj

Day 2 (11129/11550, 44 min).

This took much longer than it should have because I did it brute-force instead of thinking it out...

## day03.clj

Day 3 (7751/5831, 27 min).

This one was fun, and Clojure's native support for sets and set operations made it even easier.

## day04.clj

Day 4 (71071/68913, about 30 min).

Another sets-based problem!

## day05.clj

Day 5 (82318/79988, unknown).

Borrowed the parsing code and some other bits.

## day06.clj

Day 6 (18189/17565).

Weak. Brute forced it.

## day07.clj

Day 7 (54835/52953).

Strong. File-system parsing and depth-first searching. A typo caused me to get part 1 wrong on the first try. Ugh.

## day08.clj

Day 8 (15093/13986).

More ugh.

## day09.clj

Day 9 (49106/39353).

I had an advantage from doing it so late, that I knew what the second half was. I wrote the first half with enough engineering to handle the second as well.

## day10.clj

Day 10 (12580/11514).

Part 1 was a breeze, part 2 was another "read the letters this ASCII stream forms" kind of puzzles, which don't do much for me. But part 2 worked right the first time, no debugging except for getting the screen output readable.

## day11.clj

Day 11 (9132/7338, 2 hours 36 min).

Part 1 took about 5 minutes to crack the parsing and 1:40 more to get a working solution. Part 2 was a "math trick" problem. After my solution ran over 30 minutes without completing, I looked closer at the math and re-did parts of it. Took less than a minute, once fixed.

## day12.clj

Day 12 (7715/7799, about 2 hours 30 min).

This was tough, mentally. I got the parsing of the data done almost immediately, then struggled to remember how to do a search algorithm in Clojure. I also waffled between Dijkstra and A*, before realizing that a breadth-first-search starting from the destination point and working backwards was what I needed.

Part 2 was actually fairly easy to do, once part 1 was finished. I re-used part 1 by solving 2 as a series of point-to-point searches, then taking the shortest result.

Thus far, I haven't had the time (or energy) to do any "improved" versions of days, but I might for this one. I think I can make it a lot more understandable.

## day13.clj

Day 13 (--/--).

## day14.clj

Day 14 (--/--).

## day15.clj

Day 15 (--/--).

## day16.clj

Day 16 (--/--).

## day17.clj

Day 17 (--/--).

## day18.clj

Day 18 (--/--).

## day19.clj

Day 19 (--/--).

## day20.clj

Day 20 (--/--).

## day21.clj

Day 21 (--/--).

## day22.clj

Day 22 (--/--).

## day23.clj

Day 23 (--/--).

## day24.clj

Day 24 (--/--).

## day25.clj

Day 25 (--/--).
