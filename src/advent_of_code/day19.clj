(ns advent-of-code.day19
  (:require [advent-of-code.utils :as u]))

(defn- parse-blueprint [line]
  (let [[bp ore clay obs-1 obs-2 geo-1 geo-2] (u/parse-out-longs line)]
    [bp {:ore ore, :clay clay, :obsidian [obs-1 obs-2], :geode [geo-1 geo-2]}]))

(defn- find-best-score [mins bp]
  (let [{ore-o         :ore
         cla-o         :clay
         [obs-1 obs-2] :obsidian
         [geo-1 geo-2] :geode}   bp
        max-ore                  (max ore-o cla-o obs-1 geo-1)
        start                    [[0 0 0 0 1 0 0 0 mins]]
        queue                    (into
                                  clojure.lang.PersistentQueue/EMPTY start)]
    (loop [queue queue, best 0, seen #{}]
      (let [move (peek queue), queue (pop queue)]
        (cond
          (nil? move) best
          :else
          (let [[ore cla obs geo orbot clbot obbot gebot time] move
                best                                           (max best geo)]
            #_(if (zero? (mod (count queue) 10000))
                (prn time best (count queue)))
            (cond
              (zero? time) (recur queue best seen)
              :else
              (let [orbot     (if (>= orbot max-ore) max-ore orbot)
                    clbot     (if (>= clbot obs-2) obs-2 clbot)
                    obbot     (if (>= obbot geo-2) geo-2 obbot)
                    tmp       (- (* time max-ore) (* orbot (dec time)))
                    ore       (if (>= ore tmp) tmp ore)
                    tmp       (- (* time obs-2) (* clbot (dec time)))
                    cla       (if (>= cla tmp) tmp cla)
                    tmp       (- (* time geo-2) (* obbot (dec time)))
                    obs       (if (>= obs tmp) tmp obs)
                    cur-state [ore cla obs geo orbot clbot obbot gebot time]]
                (if (seen cur-state)
                  (recur queue best seen)
                  (let [seen      (conj seen cur-state)
                        new-moves [[(+ ore orbot) (+ cla clbot)
                                    (+ obs obbot) (+ geo gebot)
                                    orbot clbot obbot gebot (dec time)]]
                        new-moves (if (>= ore ore-o)
                                    (conj new-moves [(+ orbot (- ore ore-o))
                                                     (+ cla clbot)
                                                     (+ obs obbot)
                                                     (+ geo gebot)
                                                     (inc orbot) clbot
                                                     obbot gebot (dec time)])
                                    new-moves)
                        new-moves (if (>= ore cla-o)
                                    (conj new-moves [(+ orbot (- ore cla-o))
                                                     (+ cla clbot)
                                                     (+ obs obbot)
                                                     (+ geo gebot)
                                                     orbot (inc clbot)
                                                     obbot gebot (dec time)])
                                    new-moves)
                        new-moves (if (and (>= ore obs-1) (>= cla obs-2))
                                    (conj new-moves [(+ orbot (- ore obs-1))
                                                     (+ clbot (- cla obs-2))
                                                     (+ obs obbot)
                                                     (+ geo gebot)
                                                     orbot clbot
                                                     (inc obbot) gebot
                                                     (dec time)])
                                    new-moves)
                        new-moves (if (and (>= ore geo-1) (>= obs geo-2))
                                    (conj new-moves [(+ orbot (- ore geo-1))
                                                     (+ cla clbot)
                                                     (+ obbot (- obs geo-2))
                                                     (+ geo gebot)
                                                     orbot clbot
                                                     obbot (inc gebot)
                                                     (dec time)])
                                    new-moves)]
                    (recur (into queue new-moves) best seen)))))))))))

(defn part-1
  "Day 19 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-blueprint)
       (into {})
       (map (fn [[num bp]]
              (* num (find-best-score 24 bp))))
       (apply +)))

(defn part-2
  "Day 19 Part 2"
  [input]
  (->> input
       u/to-lines
       (take 3)
       (map parse-blueprint)
       (map last)
       (map #(find-best-score 32 %))
       (apply *)))
