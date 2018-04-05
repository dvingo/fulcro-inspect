(ns fulcro.inspect.ui.demos-cards
  (:require
    [devcards.core :refer-macros [defcard]]
    [fulcro.client.cards :refer-macros [defcard-fulcro]]
    [fulcro.client.primitives :as fp]
    [fulcro.client.alpha.localized-dom :as dom]
    [fulcro.client.mutations :as mutations]
    [fulcro.inspect.card-helpers :as card.helpers]
    [fulcro.inspect.helpers :as h]))

(fp/defsc ListItem
  [this {::keys [title]}]
  {:initial-state (fn [p] (merge {::todo-item-id (random-uuid)} p))
   :ident         [::todo-item-id ::todo-item-id]
   :query         [::todo-item-id ::title]}
  (dom/div (str title)))

(mutations/defmutation add-item [item]
  (action [env]
    (h/create-entity! env ListItem item :append :items)
    (h/swap-entity! env assoc ::title ""))
  (refresh [_] [:items]))

(def list-item (fp/factory ListItem {:keyfn ::todo-item-id}))

(fp/defsc AddItemDemo
  [this {:keys [items] ::keys [title]}]
  {:initial-state (fn [_] {::todo-app-id (random-uuid)
                           ::title ""
                           :items []})
   :query         [::todo-app-id ::title
                   {:items (fp/get-query ListItem)}]
   :ident         [::todo-app-id ::todo-app-id]}
  (dom/div
    (dom/input {:type "text"
                :value title
                :onChange #(mutations/set-string! this ::title :event %)})
    (dom/button {:onClick #(fp/transact! this [`(add-item {::title ~title})])}
      "Add item")
    (js/console.log "RENDER" items)
    (mapv list-item items)))

(defcard-fulcro add-item-demo
  (card.helpers/make-root AddItemDemo "add-item-demo"))
