(ns tic-tac-toe.core
  (:require [tic-tac-toe.utils :as u]
            [tic-tac-toe.wiki-strategy :refer [wiki-strategy]]))

(def empty-board (u/index-board
                  '[[? ? ?]
                    [? ? ?]
                    [? ? ?]]))

(def empty-game {:board empty-board
                 :player-turn 'x
                 :status :playing})


(defn winner [board]
  (let [all (u/all-lines board)
        win? (fn [[a b c]] (and (= a b c) (not= a '?)))]
    (->> (filter win? all) first first)))

(defn update-game-status [game]
  (if-let [w (winner (:board game))]
    (-> game
       (assoc :status :finished)
       (assoc :winner w))
    game))

(defn play [{:keys [board player-turn status] :as game} r c]
  (if (and (= status :playing)
           (= '? (get-in board [r c])))
    (-> game
        (update :board u/board-play [r c] player-turn)
        (update-in [:player-turn] u/other-player)
        (update-game-status))

    game))

#_(u/play-rules empty-board 'o wiki-strategy)
