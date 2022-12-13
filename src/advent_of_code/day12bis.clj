(ns advent-of-code.day12bis
  (:require [clojure.string :as str]))

(def height-map
  (apply hash-map (mapcat #(list (char (+ % 97)) %) (range 26))))

(def dirs [[-1 0] [1 0] [0 -1] [0 1]])

(defn- make-struct [lines]
  (let [S     (first (for [y (range (count lines))
                           :when (str/includes? (lines y) "S")]
                       [y (str/index-of (lines y) "S")]))
        E     (first (for [y (range (count lines))
                           :when (str/includes? (lines y) "E")]
                       [y (str/index-of (lines y) "E")]))
        field (assoc-in (assoc-in (mapv vec lines) S \a) E \z)
        grid  (set (for [y (range (count lines))
                         x (range (count (first lines)))]
                     [y x]))]
    {:S S, :E E, :field field, :grid grid}))

(defn- elevation [field pos] (height-map (get-in field pos)))

(defn- get-reachable [pos seen field grid]
  (let [height (elevation field pos)]
    (for [dir dirs
          :let [newpos (mapv + pos dir)]
          :when (and (grid newpos)
                     (not (seen newpos))
                     (>= (elevation field newpos) (dec height)))]
      newpos)))

(defn- do-search [targets data]
  (let [{E :E,
         field :field,
         grid :grid}   data]
    (loop [[cur & cs] (list (list E)), seen #{E}]
      (cond
        (targets (first cur)) (rest cur)
        :else
        (let [frontier (get-reachable (first cur) seen field grid)
              newpaths (map #(cons % cur) frontier)
              newseen  (reduce conj seen frontier)]
          (recur (concat cs newpaths) newseen))))))

(defn part-1
  "Day 12 Part 1"
  [input]
  (as-> input $
    (str/split-lines $)
    (make-struct $)
    (do-search #{(:S $)} $)
    (count $)))

(defn- find-a [field]
  (for [y (range (count field)), x (range (count (first field)))
        :when (= (get-in field [y x]) \a)]
    [y x]))

(defn part-2
  "Day 12 Part 2"
  [input]
  (as-> input $
    (str/split-lines $)
    (make-struct $)
    (do-search (set (find-a (:field $))) $)
    (count $)))
