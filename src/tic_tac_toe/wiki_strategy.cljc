(ns tic-tac-toe.wiki-strategy
  (:require [tic-tac-toe.utils :as u]))


(defn win
  "If the player has two in a row,
  they can place a third to get three in a row."
  [board player]
  (let [winner-coords (->> (u/player-holes board player)
                           (map (comp :coord meta u/hole)))]
    (when (not-empty winner-coords)
      (assoc-in board (first winner-coords) player))))

(defn block
  "If the opponent has two in a row,
  the player must play the third themselves to block the opponent."
  [board player]
  (let [looser-coords (->> (u/player-holes board (u/other-player player))
                           (map (comp :coord meta u/hole)))]
    (when (not-empty looser-coords)
      (assoc-in board (first looser-coords) player))))

(defn future-fork-coords
  "Given a board and a future board returns fork coord if it generated a fork
  for the player"
  [board future-board player]
  (let [current-holes (u/player-holes future-board player)
        future-holes (u/player-holes board player)]
    (when (>= (- (count future-holes) (count current-holes))
              2)
      (let [[cline fline] (u/first-difference current-holes future-holes)
            [ce _]  (u/first-difference cline fline)]
        (-> ce meta :coord)))))

(defn fork
  "Create an opportunity where the player has two threats to win (two non-blocked lines of 2)."
  [board player]
  (let [fork-coord (->> (u/future-boards board player)
                        (keep #(future-fork-coords board % player))
                        first)]
    (when fork-coord
      (assoc-in board fork-coord player))))

(defn block-opponent-fork-1
  "The player should create two in a row to force the opponent into defending,
   as long as it doesn't result in them creating a fork. For example,
   if X has two opposite corners and O has the center,
   O must not play a corner in order to win. (Playing a corner in this scenario creates a fork for X to win.)"
  [board player]
  ;; TODO understand this
  )

(defn block-opponent-fork-2
  "If there is a configuration where the opponent can fork, the player should block that fork."
  [board player]
  (let [enemy-fork-coord (->> (u/future-boards board (u/other-player player))
                              (keep #(future-fork-coords board % (u/other-player player)))
                              first)]
    (when enemy-fork-coord
      (assoc-in board enemy-fork-coord player))))

(defn center
  "A player marks the center. 
  If it is the first move of the game, playing on a corner gives the second player more opportunities 
  to make a mistake and may therefore be the better choice;
  however, it makes no difference between perfect players."
  [board player]
  ;; TODO understand this
  )


(defn opposite-corner
  "Opposite corner: If the opponent is in the corner, the player plays the opposite corner."
  [board player]
  (let [corners (u/corners board)
        free-corners (->> corners
                          (filter #(= % '? ))
                          (map (comp :coord meta)))
        oponent-corners (->> corners
                             (filter #(= % (u/other-player player)))
                             (map (comp :coord meta)))]
    ;; TODO, do it with oposite corner instead of first free
    (when (and (not-empty free-corners)
             (not-empty oponent-corners))
      (assoc-in board (first free-corners) player))))


(defn empty-corner
  "Empty corner: The player plays in a corner square."
  [board player]
  (let [free-corner (->> (u/corners board)
                         (filter #(= % '? ))
                         first meta :coord)]
    (when free-corner
      (assoc-in board free-corner player))))

(defn empty-side
  "Empty side: The player plays in a middle square on any of the 4 sides"
  [board player]
  (let [free-side (->> (u/sides board)
                       (filter #(= % '? ))
                       first meta :coord)]
    (when free-side
      (assoc-in board free-side player))))


(def wiki-strategy [win
                    block
                    fork
                    block-opponent-fork-1
                    block-opponent-fork-2
                    center
                    opposite-corner
                    empty-corner
                    empty-side])

(comment

  (u/try-rules '[[x ? x]
                 [? ? x]
                 [x ? ?]]
               'x
               wiki-strategy
               ))
