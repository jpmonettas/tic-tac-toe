(ns tic-tac-toe.utils)

(defn diagonals [matrix]
  "Diagonals from matrix"
  (let [index-range (range (count matrix))
        f (fn [i v] (get v i))]
    [(map f index-range matrix)
     (map f (reverse index-range) matrix)]))

(defn all-lines
  "Column, rows and diagonal for a matrix"
  [matrix]
  (let [h matrix
        v (apply (partial map list) matrix)
        d (diagonals matrix)]
    (concat h v d)))

(defn index-board
  "Add :coord [r c] to every board element meta"
  [board]
  (mapv (fn [y r]
          (mapv (fn [x elem]
                  (with-meta elem {:coord [y x]}))
                (range)
                r))
        (range)
        board))

(defn line-hole
  "Given a line returns a hole if it has one.
  A hole is only one free position being the other two used by
  the same player."
  [line]
  (let [freqs (frequencies line)]
    (and (= 2 (count freqs))
         (= 1 (get freqs '? ))
         (first (filter #(= '? %) line)))))

(def other-player '{x o
                    o x})

(defn move-number [board]
  (-> (flatten board)
      (filter #(not= '? %))
      count
      inc))

(defn first-success
  "Returns the return of the first function in fns that doesn't return nil.
  Functions are called with args"
  [[f & f-rest] & args]
  (if-let [r (apply f args)]
    r
    (when f-rest
      (recur f-rest args))))

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

(defn future-boards
  "Given a board and a player returns a lazyseq of [coord future-board]"
  [board player]
  (map (fn [b ?coord]
         [?coord (assoc-in b ?coord player)])
       (repeat board)
       (->> (flatten board)
            (filter #(= % '? ))
            (map (comp :coord meta)))))

(defn player-holes-lines
  "Given a board and a player returns all the lines that contains a hole 
  for the player. Ex [x ? x] contains a hole for x"
  [board player]
  (->> board
       all-lines
       (filter line-hole)
       (filter #(some (partial = player) %))))

(defn board-play [board coord player]
  (assoc-in board coord (with-meta player {:coord coord})))
