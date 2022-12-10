(ns advent-of-code.day10
  (:require [clojure.string :as str]))

(defn- to-inst-stream [insts]
  (flatten (map #(str/split % #" ") insts)))

(defn- find-signal-strengths [freqs insts]
  (let [insts (to-inst-stream insts)]
    (loop [[i & is] insts, X 1, strengths [], cycle 1]
      (cond
        (nil? i)             (map #(strengths (dec %)) freqs)
        (re-find #"-?\d+" i) (let [newX (+ X (parse-long i))]
                               (recur is newX (conj strengths (* cycle X))
                                      (inc cycle)))
        :else                (recur is X (conj strengths (* cycle X))
                                    (inc cycle))))))

(defn part-1
  "Day 10 Part 1"
  [input]
  (->> input
       str/split-lines
       (find-signal-strengths [20 60 100 140 180 220])
       (apply +)))

(defn- make-sprite [x] (set (list (dec x) x (inc x))))

(defn- pixel-at [sprite row] (if (sprite row) "#" "."))

(defn- to-raster-lines [insts]
  (loop [[i & is] insts, X 1, sprite (make-sprite X), pixels [], row 0]
    (cond
      (nil? i)             (partition 40 pixels)
      (re-find #"-?\d+" i) (let [newX      (+ X (parse-long i))
                                 newSprite (make-sprite newX)]
                             (recur is newX newSprite
                                    (conj pixels (pixel-at sprite row))
                                    (mod (inc row) 40)))
      :else                (recur is X sprite
                                  (conj pixels (pixel-at sprite row))
                                  (mod (inc row) 40)))))

(defn part-2
  "Day 10 Part 2"
  [input]
  (->> input
       str/split-lines
       to-inst-stream
       to-raster-lines
       (map #(apply str %))
       (str/join "\n")))
