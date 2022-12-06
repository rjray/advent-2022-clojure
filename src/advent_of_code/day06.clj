(ns advent-of-code.day06
  (:require [clojure.string :as str]))

(defn is-start [s]
  (= 4 (count (set (take 4 (reverse s))))))

(defn find-start [s]
  (loop [c 4]
    (cond
      (= c (count s))       nil
      (is-start (take c s)) c
      :else                 (recur (inc c)))))

(defn part-1
  "Day 06 Part 1"
  [input]
  (->> input
       str/split-lines
       first
       find-start))

(defn is-start2 [s]
  (= 14 (count (set (take 14 (reverse s))))))

(defn find-start2 [s]
  (loop [c 14]
    (cond
      (= c (count s))       nil
      (is-start2 (take c s)) c
      :else                 (recur (inc c)))))

(defn part-2
  "Day 06 Part 2"
  [input]
  (->> input
       str/split-lines
       first
       find-start2))
