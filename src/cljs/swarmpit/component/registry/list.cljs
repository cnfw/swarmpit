(ns swarmpit.component.registry.list
  (:require [material.component :as comp]
            [material.icon :as icon]
            [swarmpit.component.state :as state]
            [swarmpit.routes :as routes]
            [clojure.string :as string]
            [rum.core :as rum]))

(def cursor [:page :registry :list :filter])

(def headers ["Name" "Url" "Secure"])

(defn- filter-items
  "Filter list items based on given predicate"
  [items predicate]
  (filter #(string/includes? (:name %) predicate) items))

(def render-item-keys
  [[:name] [:url] [:withAuth]])

(defn- render-item
  [item]
  (let [value (val item)]
    (case (key item)
      :withAuth (if value
                  (comp/svg icon/ok)
                  "")
      value)))

(defn- onclick-handler
  [item]
  (routes/path-for-frontend :registry-info {:id (:_id item)}))

(rum/defc registry-list < rum/reactive [items]
  (let [{:keys [name]} (state/react cursor)
        filtered-items (filter-items items name)]
    [:div
     [:div.form-panel
      [:div.form-panel-left
       (comp/panel-text-field
         {:hintText "Filter by name"
          :onChange (fn [_ v]
                      (state/update-value [:name] v cursor))})]
      [:div.form-panel-right
       (comp/mui
         (comp/raised-button
           {:href    (routes/path-for-frontend :registry-create)
            :label   "Add"
            :primary true}))]]
     (comp/list-table headers
                      (sort-by :name filtered-items)
                      render-item
                      render-item-keys
                      onclick-handler)]))

(defn- init-state
  []
  (state/set-value {:name ""} cursor))

(defn mount!
  [items]
  (init-state)
  (rum/mount (registry-list items) (.getElementById js/document "content")))