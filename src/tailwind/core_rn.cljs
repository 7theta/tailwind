;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   MIT License (https://opensource.org/licenses/MIT) which can also be
;;   found in the LICENSE file at the root of this distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.
(ns tailwind.core-rn
  (:require [integrant.core :as ig]
            [utilis.js :as j]
            [clojure.string :as st]
            [tailwind-rn :as tw-rn]))

(def ^:private created (atom nil))

(defmethod ig/init-key :tailwind/config
  [_ {:keys [styles]}]
  (reset! created (j/call tw-rn :create (clj->js styles))))

(defn tw
  [classes]
  (let [class-str (->> classes (filter keyword?) (map name) (st/join " " ))]
    (if @created
      (j/call @created :tailwind class-str)
      (tw-rn class-str))))

(defn class->color
  [class]
  (when class
    (j/call (or @created tw-rn) :getColor (name class))))
