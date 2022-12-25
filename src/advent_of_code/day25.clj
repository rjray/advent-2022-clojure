(ns advent-of-code.day25
  (:require [advent-of-code.utils :as u]
            [clojure.math.numeric-tower :as math :refer [expt]]))

(def digits {\2 2, \1 1, \0 0, \- -1, \= -2})
(def chs [\0 \1 \2 \= \-])
(def offs [0 -1 -2 2 1])

(defn- from-snafu [numstr]
  (let [num   (reverse (seq numstr))
        pairs (map #(list %1 %2) (iterate inc 0) num)]
    (apply + (for [[pow val] pairs]
               (* (digits val )(expt 5 pow))))))

(defn- to-snafu [num]
  (loop [n num, ret ()]
    (cond
      (zero? n) (apply str ret)
      :else     (let [d (mod n 5)]
                  (recur (quot (+ n (offs d)) 5) (cons (chs d) ret))))))

(defn part-1
  "Day 25 Part 1"
  [input]
  (->> input
       u/to-lines
       (map from-snafu)
       (reduce +)
       to-snafu))

(defn part-2
  "Day 25 Part 2"
  [input]
  input)
