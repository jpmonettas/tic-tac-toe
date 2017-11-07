(ns ^:figwheel-always tic-tac-toe.view
  (:require [tic-tac-toe.core :refer [play empty-game]]
            [tic-tac-toe.utils :as u]
            [tic-tac-toe.wiki-strategy :refer [wiki-strategy]]
            [reagent.core :as reagent :refer [atom]]))



(enable-console-print!)

(defonce game-states (atom [empty-game]))

(defn forward-state [gs row col]
  (let [game' (-> (last gs)
                  (play row col))
        [ai-row ai-col] (u/first-success wiki-strategy
                                         (:board game')
                                         (:player-turn game'))
        game'' (play game' ai-row ai-col)]
    (conj gs game'')))
 
(defn cell-click [e]
  (let [row (-> e .-target .-dataset .-row js/parseInt)
        col (-> e .-target .-dataset .-col js/parseInt)]
    (swap! game-states forward-state row col)))

(defn row [r-idx row-v]
  [:tr {:key r-idx}
   (map-indexed
    (fn [i v]
      [:td {:data-row r-idx
                   :data-col i
                   :on-click cell-click
                   :class (name v)
                   :key i}
              (cond 
                (= v 'x) "X"
                (= v 'o) "O"
                (= v '?) "") ])
    row-v)])

(defn undo-game-move [_]
  (swap! game-states (comp vec butlast)))

(defn reset-game [_]
  (reset! game-states [empty-game]))

(defn board []
  (let [game (last @game-states)]
    (println game)
   [:div
    (when (= :finished (:status game))
      [:h1 (str (name (:winner game)) " WINS !")])
    [:button {:on-click undo-game-move} "Undo"]
    [:button {:on-click reset-game} "Restart"]
    [:table 
     (map-indexed row (:board game))]]))


(reagent/render-component [board]
                          (. js/document (getElementById "app")))


(defn on-js-reload [])
