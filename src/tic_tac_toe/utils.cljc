(ns tic-tac-toe.utils)

(defn diagonals [matrix]
  "extract diagonals from matrix"
  (let [index-range (range (count matrix))
        f (fn [i v] (get v i))]
    [(map f index-range matrix)
     (map f (reverse index-range) matrix)]))

(defn all-lines [board]
  (let [h board
        v (apply (partial map list) board)
        d (diagonals board)]
    (concat h v d)))

(defn index-board [board]
  (mapv (fn [y r]
          (mapv (fn [x elem]
                  (with-meta elem {:coord [y x]}))
                (range)
                r))
        (range)
        board))

(defn hole [line]
  (let [freqs (frequencies line)]
    (and (= 2 (count freqs))
         (= 1 (get freqs '? ))
         (first (filter #(= '? %) line)))))

(def other-player '{x o
                    o x})

(defn first-difference
  "Given two sequences returns the first pair of elements that 
  are different"
  [seq1 seq2]
  (->> (map vector seq1 seq2)
       (drop-while (fn [[a b]] (= a b)))
       first))

(defn move-number [board]
  (-> (flatten board)
      (filter #(not= '? %))
      count
      inc))

(defn try-rules [board player [rule & rest-of-rules]]
  (if-let [new-board (rule board player)]
    new-board
    (when rest-of-rules
      (recur board player rest-of-rules))))

(defn corners [board]
  (let [max (dec (count board))]
    [(get-in board [0 0])
     (get-in board [0 max])
     (get-in board [max max])
     (get-in board [max 0])]))

;; TODO How to make this independent of size??
(defn sides [board]
  [(get-in board [0 1])
   (get-in board [1 0])
   (get-in board [2 1])
   (get-in board [1 2])])

(defn future-boards [board player]
  (map (fn [b ?coord]
         (assoc-in b ?coord player))
       (repeat board)
       (->> (flatten board)
            (filter #(= % '? ))
            (map (comp :coord meta)))))

(defn player-holes [board player]
  (->> board
       all-lines
       (filter hole)
       (filter #(some (partial = player) %))))
