(ns reagent-lifecyle-example.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(def app-state (atom {:text "Hello world!"
                          :counter1 0
                          :counter2 0}))

(defn verbose-component
  [label n]
  (println "[verbose-component] fn call")
  (reagent/create-class
    {:component-did-mount
     #(println "[verbose-component] component-did-mount (label=" label ")" )

     :component-will-mount
     #(println "[verbose-component] component-will-mount (label=" label ")")

     :reagent-render ;; Note:  is not :render
     (fn [label n] ;; remember to repeat parameters
       (println "[verbose-component] reagent-render (label=" label ")")
       [:div
        [:h3 "Verbose Component (" label ")"]
        [:div "n = " (str n)]])}))

(defn hello-world []
  [:div
   [:h1 "reagent-lifecyle-example"]
   [:div "@app-state =" (str @app-state)]

   (into [:div] (map (fn [k]
                       [:button {:on-click (fn [e]
                                             (println "inc " (str k))
                                             (swap! app-state assoc k (inc (get @app-state k))))} (str "inc " k)])
                     [:counter1 :counter2]))

   [:div
    [:h2 "Verbose Component 1"]
    [verbose-component "one" (@app-state :counter1)]]
   [:div
    [:h2 "Verbose Component 2"]
    [verbose-component "two" (@app-state :counter2)]]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
