(ns advent-of-code.day04
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn contained-by? [a b]
  (let [i (set/intersection a b)]
    (or (= a i) (= b i))))

(defn str2sets [s]
  (let [[s1 s2]   (str/split s #",")
        [r11 r12] (map #(Integer/parseInt %) (str/split s1 #"-"))
        [r21 r22] (map #(Integer/parseInt %) (str/split s2 #"-"))
        set1      (set (range r11 (inc r12)))
        set2      (set (range r21 (inc r22)))]
    (list set1 set2)))

(defn part-1
  "Day 04 Part 1"
  [input]
  (->> input
       str/split-lines
       (map str2sets)
       (map #(contained-by? (first %) (last %)))
       (filter identity)
       count))

(defn overlap? [a b]
  (not (empty? (set/intersection a b))))

(defn part-2
  "Day 04 Part 2"
  [input]
  (->> input
       str/split-lines
       (map str2sets)
       (map #(overlap? (first %) (last %)))
       (filter identity)
       count))
