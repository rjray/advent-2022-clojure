(ns advent-of-code.day16
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]
            [clojure.set :as set]))

(def ^:private line-re #"Valve (\w+).*rate=(\d+);.*valves? (.*)")

(defn- parse-line [line]
  (let [[_ valve rate tunnels] (re-find line-re line)
        tunnels                (set (str/split tunnels #", "))]
    (list valve {:rate (parse-long rate), :tunnels tunnels})))

(defn- to-graph [lines]
  (reduce (fn [coll pair]
            (assoc coll (first pair) (last pair)))
          {} lines))

(defn- get-nonzero-valves [graph]
  (cons "AA" (filter #(pos? (:rate (graph %))) (keys graph))))

(defn- shortest-path [start end graph]
  (loop [[[position steps] & qs] [[start 0]], cost {}]
    (cond
      (nil? position)                     (cost end)
      (= position end)                    (cost end)
      (> steps (get cost position ##Inf)) (recur qs cost)
      :else
      (let [nsteps    (inc steps)
            tunnels   (:tunnels (graph position))
            [new-queue new-cost]
            (loop [[t & ts] tunnels, queue qs, cost cost]
              (cond
                (nil? t)                      [queue cost]
                (< nsteps (get cost t ##Inf)) (recur ts
                                                     (concat queue
                                                             (list [t nsteps]))
                                                     (assoc cost
                                                            t nsteps))
                :else                         (recur ts queue cost)))]
        (recur new-queue new-cost)))))

(defn- shortest-connections [valves graph]
  (loop [[s & ss] valves, shortest {}]
    (cond
      (nil? s) shortest
      :else
      (recur ss (loop [[e & es] ss, sh shortest]
                  (cond
                    (nil? e) sh
                    :else    (let [cost (shortest-path s e graph)]
                               (recur es
                                      (assoc-in (assoc-in sh [s e] cost)
                                                [e s] cost)))))))))

(defn- traverse-all [shortest graph time]
  (let [start [["AA" 0 time #{}]]
        queue (into clojure.lang.PersistentQueue/EMPTY start)]
    (loop [queue queue, paths {}]
      (let [[pos acc-flow time seen] (peek queue), queue (pop queue)]
        #_(if (zero? (mod (count queue) 1000))
            (prn time (count queue)))
        (cond
          (nil? pos) paths
          :else
          (let [neighbors (sort (for [n (keys (shortest pos))
                                      :when (and (not (seen n))
                                                 (< (get-in shortest
                                                            [pos n]) time))]
                                  n))
                paths     (if (< (get paths seen -1) acc-flow)
                            (assoc paths seen acc-flow)
                            paths)
                new-moves (loop [[n & ns] neighbors, moves []]
                            (cond
                              (nil? n) moves
                              :else
                              (recur ns
                                     (let [new-time (- time
                                                       (get-in shortest
                                                               [pos n]) 1)
                                           new-flow (* (:rate (graph n))
                                                       new-time)
                                           new-seen (conj seen n)]
                                       (conj moves [n (+ acc-flow new-flow)
                                                    new-time new-seen])))))]
            (recur (into queue new-moves) paths)))))))

(defn- solve [time graph]
  (let [non-zero       (get-nonzero-valves graph)
        shortest-paths (shortest-connections non-zero graph)
        paths          (traverse-all shortest-paths graph time)]
    paths))

(defn part-1
  "Day 16 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       to-graph
       (solve 30)
       vals
       (apply max)))

(defn- best-disjoint [paths]
  (for [a (keys paths), b (keys paths)
        :when (and (not= a b)
                   (empty? (set/intersection a b)))]
    (+ (paths a) (paths b))))

(defn part-2
  "Day 16 Part 2"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       to-graph
       (solve 26)
       best-disjoint
       (apply max)))
