(ns advent-of-code.day02
  (:require [clojure.string :as str]))

(def scores {"X" 1, "Y" 2, "Z", 3})
(def results {:lose 0, :draw 3, :win 6})
(def shapes {"A" :rock, "B" :paper, "C" :scissors,
             "X" :rock, "Y" :paper, "Z" :scissors})

(defn result [a b]
  (let [s1 (shapes a), s2 (shapes b)]
    (cond
      (and (= s1 :rock)
           (= s2 :paper))    :win
      (and (= s1 :rock)
           (= s2 :scissors)) :lose
      (and (= s1 :paper)
           (= s2 :scissors)) :win
      (and (= s1 :paper)
           (= s2 :rock))     :lose
      (and (= s1 :scissors)
           (= s2 :rock))     :win
      (and (= s1 :scissors)
           (= s2 :paper))    :lose
      :else                  :draw)))

(defn score [line]
  (let [[a b] (str/split line #" ")]
    (+ (scores b) (results (result a b)))))

(defn part-1
  "Day 02 Part 1"
  [input]
  (->> input
       str/split-lines
       (map score)
       (apply +)))

(def outcomes {"X" :lose, "Y" :draw, "Z" :win})

(defn pick-move [a b]
  (let [out (outcomes b), move (shapes a)]
    (cond
      (and (= move :rock)
           (= out :draw))     "X"
      (and (= move :rock)
           (= out :win))      "Y"
      (and (= move :rock)
           (= out :lose))     "Z"
      (and (= move :paper)
           (= out :draw))     "Y"
      (and (= move :paper)
           (= out :win))      "Z"
      (and (= move :paper)
           (= out :lose))     "X"
      (and (= move :scissors)
           (= out :draw))     "Z"
      (and (= move :scissors)
           (= out :win))      "X"
      (and (= move :scissors)
           (= out :lose))     "Y")))

(defn get-score [line]
  (let [[a b] (str/split line #" ")
        mine  (pick-move a b)]
    (+ (scores mine) (results (result a mine)))))

(defn part-2
  "Day 02 Part 2"
  [input]
  (->> input
       str/split-lines
       (map get-score)
       (apply +)))
