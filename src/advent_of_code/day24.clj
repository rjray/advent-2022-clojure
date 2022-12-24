(ns advent-of-code.day24
  (:require [advent-of-code.utils :as u]))

(def e-moves [[0 0] [0 1] [1 0] [0 -1] [-1 0]])
(def b-moves {\> [0 1], \v [1 0], \< [0 -1], \^ [-1 0]})

(defn- to-struct [lines]
  (let [data   (mapv #(vec (seq %)) lines)
        height (- (count data) 2)
        width  (- (count (first data)) 2)
        blizz  (vec (for [y (range 1 (inc height)), x (range 1 (inc width))
                          :let [ch (get-in data [y x])]
                          :when (not= ch \.)]
                      (list [y x] (b-moves ch))))
        walls  (into #{} (for [y (range (count data))
                               x (range (count (first data)))
                               :when (= \# (get-in data [y x]))]
                           [y x]))]
    {:blizz blizz, :walls walls, :height height, :width width, :minutes 0,
     :begin [0 1], :target [(inc height) width]}))

(defn- advance [blizz height width]
  (vec (for [[[y x] [dy dx]] blizz
             :let [ny (+ y dy)
                   nx (+ x dx)
                   ny (inc (mod (dec ny) height))
                   nx (inc (mod (dec nx) width))]]
         (list [ny nx] [dy dx]))))

(defn- get-blizz-sq [blizz] (into #{} (map first blizz)))

(defn get-moves [pos walls danger]
  (for [move e-moves
        :let [newpos (mapv + pos move)]
        :when (and (>= (first newpos) 0)
                   (not (walls newpos))
                   (not (danger newpos)))]
    newpos))

(defn- find-shortest [state]
  (let [{blizz :blizz
         walls :walls
         height :height
         width :width
         begin :begin
         target :target
         minutes :minutes} state]
    (loop [state state, search #{begin}]
      (cond
        (search target) state
        :else
        (let [blizz'  (advance (:blizz state) height width)
              danger  (get-blizz-sq blizz')
              search' (into #{} (for [pos search
                                      move (get-moves pos walls danger)
                                      :when (<= (first move) (inc height))]
                                  move))]
          (recur (-> state
                     (assoc :blizz blizz')
                     (update :minutes inc))
                 search'))))))

(defn part-1
  "Day 24 Part 1"
  [input]
  (-> input
      u/to-lines
      to-struct
      find-shortest
      :minutes))

(defn- swap-endpoints [state]
  (assoc state :begin (:target state) :target (:begin state)))

(defn part-2
  "Day 24 Part 2"
  [input]
  (-> input
      u/to-lines
      to-struct
      find-shortest
      swap-endpoints
      find-shortest
      swap-endpoints
      find-shortest
      :minutes))
