(ns tic-tac-toe.test.core
  (:require [tic-tac-toe.core :as c]
            [clojure.test :refer :all]))

(deftest play
  (is (= (c/play {:board [[:e :e :e]
                          [:e :e :e]
                          [:e :e :e]]
                  :player-turn :x
                  :status :playing} 0 0)
         {:board [[:x :e :e]
                  [:e :e :e]
                  [:e :e :e]]
          :player-turn :o
          :status :playing}))

  (is (= (c/play {:board [[:e :e :e]
                          [:e :e :e]
                          [:e :e :e]]
                  :player-turn :o
                  :status :playing} 0 0)
         {:board [[:o :e :e]
                  [:e :e :e]
                  [:e :e :e]]
          :player-turn :x
          :status :playing}))

  (is (= (c/play {:board [[:x :e :e]
                          [:e :x :e]
                          [:e :e :e]]
                  :player-turn :x
                  :status :playing} 2 2)
         {:board [[:x :e :e]
                  [:e :x :e]
                  [:e :e :x]]
          :player-turn :o
          :status :finished
          :winner :x})))


(deftest winner 
  (is (= (c/winner [[:x :e :e]
                    [:e :x :e]
                    [:e :e :x]])
         :x))

  (is (= (c/winner [[:x :e :e]
                    [:e :e :e]
                    [:e :e :e]])
         nil))

  (is (= (c/winner [[:o :o :o]
                    [:e :e :e]
                    [:e :e :e]])
         :o)))
