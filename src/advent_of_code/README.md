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

## day12bis.clj

This is a cleaned-up version, one that gets rid of some redundancy that came from me misunderstanding how to do a breadth-first search properly. It's shorter and much faster (particular on part 2) than the original.

## day13.clj

Day 13 (5740/5813).

Interesting. Though I haven't used [EDN](http://edn-format.org/) before, I immediately recognized the input format as being EDN-compliant. That simplified the parsing immensely. Getting part 1 to work was a little tricky, but once it was done it was a small wrapper to turn the Boolean comparison into something that `sort` could use for ordering and part 2 was done.

## day14.clj

Day 14 (24385/23669).

Another interesting one. Started much later than normal, hence the high numbers. Part 1 went fine but a silly assumption on part 2 led to some lengthy debugging.

## day15.clj

Day 15 (26462/22531).

Part 1 was a wreck of a brute-force approach that led to my second incorrect answer because of an algorithmic error that took forever to solve. Part 2 was only solved by converting a Python solution. Not my favorite day.

## day15bis.cls

Some small clean-up on part 2, changing a `for`-comprehension to a `loop` so that it terminates before reaching 4000000. Didn't really help much, because the matching value of `y` was pretty close to 4000000 anyway.

## day16.clj

Day 16 (16555/11496).

NGL... I gave up on this and used someone else's Python solution just to get past it. The code I'm checking in is incomplete and incorrect for part 1. I am not happy about this.

(**Edit: 12/19**: Having solved day 19 by use of the `PersistentQueue` interface, I returned to this one. I was able to get the correct answers for both part 1 and part 2 with no change to my code other than using `PersistentQueue` in place of my sequence-based queue. Feeling marginally better, now.)

## day17.clj

Day 17 (12956/8238).

Works. I borrowed the representation of the blocks and some of the logic from `nbardiuk`. The second half is a "classic" AoC twist; do what you did in part 1, but do it an ungodly number of times. The answer to these is (almost) always to look for a cycle and multiply things out.

## day18.clj

Day 18 (4103/3502).

My best finish so far this year (not bad, since I'm usually not starting right at the release of each day). Part 1 was straightforward, but I wasted time on part 2 on a flawed analysis. When I saw that the top placers on the reddit thread had all used some variant of BFS, I rewrote part 2 and got the answer.

## day19.clj

Day 19 (7885/6912).

I had to take a lot from a Python solution to get through this one. The heuristics that were needed just escaped me, for the most part. I also got the same stack-overflow errors I had gotten on day 16. Advice from the Clojure Slack lead me to the [PersistentQueue](https://www.javadoc.io/doc/org.clojure/clojure/1.11.1/clojure/lang/PersistentQueue.html) interface, which beat back the stack overflows. However, I did have to increase the JVM heap allocation to get through part 2.

## day20.clj

Day 20 (10730/9860).

Ahh, nothing says AoC like a problem based around modulo math. I struggled with the math for the `mix` operation, then slept on it. After getting up and seeing a comment by someone from NVIDIA, I got `mix` working and part 1 went quickly. Part 2 was surprisingly easy by comparison, because my approach to part 1 kept the original positions of each number in the data.

## day21.clj

Day 21 (14470/11489).

The inevitable parsing-mathematical-expressions problem. Part 1 was very straightforward, but part 2's twist was more of a challenge. I didn't think about the overall function being linear until I read some comments on the Clojure Slack's AoC channel. I imagine my part 2 looks a lot like others' part 2 code, just because that factor reduces the problem to fairly simple steps.

## day22.clj

Day 22 (2944/6251).

Part 1 went fairly well, and I got my best-yet finishing placement for 2022. Part 2, though, was twisty af. I ended up having to make a paper cube like almost everyone else. In the end, though, I only had one actual bug in the part 2 code and that was using the wrong variable name in a `cond` block. That said, part 2 is way more brute-force than I like. But it's after 3PM and I have no interest in trying to optimize the code, at least not right now.

## day23.clj

Day 23 (10535/10289).

This should have been a breeze: it's a familiar pattern, and one that I've solved many times in past AoC years. But a dumb mistake on the east/west movement and I was forever trying to debug part 1. Once done, part 2 took less than 5 minutes.

## day24.clj

Day 24 (8451/8206).

A search problem with a twist: the "maze" changes each round. My first attempt at a BFS blew up my heap (again, despite having already been upped to 2Gig). A second attempt at BFS handled the heap issues, but took way longer than it should so it got killed. Finally, a simplified search algorithm did the trick, though I was worried it could get stuck in a loop when it didn't.

Part 2 was surprisingly low-key: just got from point A to B as in part 1, then from B to A and A back to B. I had to re-wire some of the functions to carry more state around, and I had to add a boundary-check to keep from moving too far in the Y axis. But the answer came back quickly and (more important) correctly.

## day25.clj

Day 25 (--/--).
