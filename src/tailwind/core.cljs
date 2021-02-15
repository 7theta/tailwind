;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   MIT License (https://opensource.org/licenses/MIT) which can also be
;;   found in the LICENSE file at the root of this distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns tailwind.core
  (:require [integrant.core :as ig]
            [utilis.js :as j]
            [clojure.string :as st]))

(declare created tw-rn colors)

(defmethod ig/init-key :tailwind/config
  [_ {:keys [styles]}]
  (reset! created (j/call tw-rn :create (clj->js styles))))

(defn tw
  [classes]
  (let [class-str (->> classes (filter keyword?) (map name) (st/join " " ))]
    (cond-> class-str
      (= :react-native (j/platform)) ((or (:tailwind @created) tw-rn)))))

(defn class->color
  [class]
  (if (= :react-native (j/platform))
    (j/call (or @created tw-rn) :getColor (name class))
    (j/get-in colors (->> (st/split (name class) #"\-")
                          (remove #{"bg" "text" "border"})))))


;;; Private

(defn- try-require [s]
  (when (exists? js/require)
    (try
      (js/require s)
      (catch :default _
        nil))))

(def ^:private created (atom nil))
(def ^:private tw-rn (try-require "tailwind-rn"))
(def ^:private colors (try-require "tailwindcss/colors"))
