;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   MIT License (https://opensource.org/licenses/MIT) which can also be
;;   found in the LICENSE file at the root of this distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns tailwind.core
  (:require ["tailwindcss/colors" :as colors]
            [utilis.js :as j]
            [clojure.string :as st]))

(def ^:private tw-rn
  (when (exists? js/require) (js/require "tailwind-rn")))

(defn tw
  [classes]
  (let [class-str (st/join " " (map name classes))]
    (cond-> class-str
      (= :react-native (j/platform)) tw-rn)))

(def ^:private get-color-rn
  (when (exists? js/require) (.-getColor (js/require "tailwind-rn"))))

(defn class->color
  [class]
  (case (j/platform)
    :web
    (j/get-in colors (->> (st/split (name class) #"\-")
                          (remove #{"bg" "text" "border"})))
    :react-native
    (get-color-rn class)))
