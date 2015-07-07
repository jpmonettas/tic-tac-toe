(ns tic-tac-toe.core)

(def other-player {:x :o :o :x})


(defn winner [board]
  (let [h board
        v (apply (partial map list) board)
        d (let [[[a _ b]
                 [_ c _]
                 [d _ e]] board] [[a c e] [b c d]])
        all (concat h v d)
        win? (fn [[a b c]] (and (= a b c) (not= a :e)))]
    (->> (filter win? all) first first)))

(defn update-game-status [game]
  (if-let [w (winner (:board game))]
    (-> game
       (assoc :status :finished)
       (assoc :winner w))
    game))

(defn play [{:keys [board player-turn status]:as game} r c]
  (if (and (= status :playing)
         (= :e (get-in board [r c])))
    (-> game
       (assoc-in [:board r c] player-turn)
       (update-in [:player-turn] other-player)
       (update-game-status))

    game))


 
