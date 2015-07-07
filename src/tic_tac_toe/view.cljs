(ns ^:figwheel-always tic-tac-toe.view
    (:require [tic-tac-toe.core :refer [play other-player]]
            [reagent.core :as reagent :refer [atom]]))



(enable-console-print!)

(defonce game-states (atom [{:board [[:e :e :e]
                                     [:e :e :e]
                                     [:e :e :e]]
                             :player-turn :x
                             :status :playing}]))


(defn cell-click [e]
  (let [row (-> e .-target .-dataset .-row js/parseInt)
        col (-> e .-target .-dataset .-col js/parseInt)]
    (swap! game-states (fn [gs] (conj gs (play (last gs) row col))))))

(defn row [r-idx row-v]
  [:tr
   (map-indexed
    (fn [i v][:td {:data-row r-idx
                   :data-col i
                   :on-click cell-click
                   :class (name v)}
              (case v
                :x "X"
                :o "O"
                :e "") ])
    row-v)])

(defn undo-game-move [_]
  (swap! game-states (comp vec butlast)))

(defn board []
  (let [game (last @game-states)]
    (println game)
   [:div
    (when (= :finished (:status game))
      [:h1 (str (name (:winner game)) " WINS !")])
    [:button {:on-click undo-game-move} "Undo"]
    [:table 
     (map-indexed row (:board game))]]))


(reagent/render-component [board]
                          (. js/document (getElementById "app")))


(defn on-js-reload [])
