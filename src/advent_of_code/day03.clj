(ns advent-of-code.day03
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn find-dup [s]
  (let [seqs (seq s)
        [aseq bseq] (partition (/ (count seqs) 2) seqs)]
    (first (set/intersection (set aseq) (set bseq)))))

(defn find-pri [c]
  (let [pri (- (int c) 64)]
    (cond
      (< pri 27) (+ pri 26)
      :else      (- pri 32))))

(defn part-1
  "Day 03 Part 1"
  [input]
  (->> input
       str/split-lines
       (map find-dup)
       (map find-pri)
       (apply +)))

(defn find-common [trio]
  (let [[a b c] (map set trio)]
    (first (set/intersection a b c))))

(defn part-2
  "Day 03 Part 2"
  [input]
  (->> input
       str/split-lines
       (partition 3)
       (map find-common)
       (map find-pri)
       (apply +)))
