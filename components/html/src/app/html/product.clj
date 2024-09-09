(ns app.html.product
  (:require [app.product.interface :as p]))

(defn product-data [product]
  [:div
   {:class "lg:flex lg:items-center lg:justify-between"}
   [:div
    {:class "min-w-0 flex-1"}
    [:h2
     {:class
      "text-2xl font-bold leading-7 text-gray-900 sm:truncate sm:text-3xl sm:tracking-tight"}
     (:description product)]
    [:div
     {:class
      "mt-1 flex flex-col sm:mt-0 sm:flex-row sm:flex-wrap sm:space-x-6"} 
     [:div
      {:class "mt-2 flex items-center text-sm text-gray-500"}
      [:svg
       {:class "mr-1.5 h-5 w-5 flex-shrink-0 text-gray-400",
        :viewBox "0 0 20 20",
        :fill "currentColor",
        :aria-hidden "true"}
       [:path
        {:d
         "M10.75 10.818v2.614A3.13 3.13 0 0011.888 13c.482-.315.612-.648.612-.875 0-.227-.13-.56-.612-.875a3.13 3.13 0 00-1.138-.432zM8.33 8.62c.053.055.115.11.184.164.208.16.46.284.736.363V6.603a2.45 2.45 0 00-.35.13c-.14.065-.27.143-.386.233-.377.292-.514.627-.514.909 0 .184.058.39.202.592.037.051.08.102.128.152z"}]
       [:path
        {:fill-rule "evenodd",
         :d
         "M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-8-6a.75.75 0 01.75.75v.316a3.78 3.78 0 011.653.713c.426.33.744.74.925 1.2a.75.75 0 01-1.395.55 1.35 1.35 0 00-.447-.563 2.187 2.187 0 00-.736-.363V9.3c.698.093 1.383.32 1.959.696.787.514 1.29 1.27 1.29 2.13 0 .86-.504 1.616-1.29 2.13-.576.377-1.261.603-1.96.696v.299a.75.75 0 11-1.5 0v-.3c-.697-.092-1.382-.318-1.958-.695-.482-.315-.857-.717-1.078-1.188a.75.75 0 111.359-.636c.08.173.245.376.54.569.313.205.706.353 1.138.432v-2.748a3.782 3.782 0 01-1.653-.713C6.9 9.433 6.5 8.681 6.5 7.875c0-.805.4-1.558 1.097-2.096a3.78 3.78 0 011.653-.713V4.75A.75.75 0 0110 4z",
         :clip-rule "evenodd"}]]
      (:unit-price product)]]]
   [:div {:class "mt-5 flex lg:ml-4 lg:mt-0"} 
    [:span
     {:class "sm:ml-3"}
     (if (:selected product)
       [:div {:class "flex justify-center items-center h-9"}
        [:svg
         {:class "-ml-0.5 mr-1.5 h-5 w-5",
          :viewBox "0 0 20 20",
          :fill "currentColor",
          :aria-hidden "true"}
         [:path
          {:fill-rule "evenodd",
           :d
           "M16.704 4.153a.75.75 0 01.143 1.052l-8 10.5a.75.75 0 01-1.127.075l-4.5-4.5a.75.75 0 011.06-1.06l3.894 3.893 7.48-9.817a.75.75 0 011.05-.143z",
           :clip-rule "evenodd"}]]]
       [:button
        {:type "button",
         :class "inline-flex items-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
         :hx-get (str "/products/" (:article-id product))
         :hx-target "this"
         :hx-swap "outerHTML"}
        [:svg
         {:class "-ml-0.5 mr-1.5 h-5 w-5",
          :viewBox "0 0 20 20",
          :fill "currentColor",
          :aria-hidden "true"}
         [:path
          {:fill-rule "evenodd",
           :d
           "M16.704 4.153a.75.75 0 01.143 1.052l-8 10.5a.75.75 0 01-1.127.075l-4.5-4.5a.75.75 0 011.06-1.06l3.894 3.893 7.48-9.817a.75.75 0 011.05-.143z",
           :clip-rule "evenodd"}]]
        "Auswählen"])]
    [:div
     {:class "relative ml-3 sm:hidden"}
     [:button
      {:type "button",
       :class
       "inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:ring-gray-400",
       :id "mobile-menu-button",
       :aria-expanded "false",
       :aria-haspopup "true"}
      "More"
      [:svg
       {:class "-mr-1 ml-1.5 h-5 w-5 text-gray-400",
        :viewBox "0 0 20 20",
        :fill "currentColor",
        :aria-hidden "true"}
       [:path
        {:fill-rule "evenodd",
         :d
         "M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z",
         :clip-rule "evenodd"}]]]
     (comment
       "Dropdown menu, show/hide based on menu state.\n\n        Entering: \"transition ease-out duration-200\"\n          From: \"transform opacity-0 scale-95\"\n          To: \"transform opacity-100 scale-100\"\n        Leaving: \"transition ease-in duration-75\"\n          From: \"transform opacity-100 scale-100\"\n          To: \"transform opacity-0 scale-95\"")
     [:div
      {:class
       "absolute right-0 z-10 -mr-1 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none",
       :role "menu",
       :aria-orientation "vertical",
       :aria-labelledby "mobile-menu-button",
       :tabindex "-1"}
      (comment "Active: \"bg-gray-100\", Not Active: \"\"")
      [:a
       {:href "#",
        :class "block px-4 py-2 text-sm text-gray-700",
        :role "menuitem",
        :tabindex "-1",
        :id "mobile-menu-item-0"}
       "Edit"]
      [:a
       {:href "#",
        :class "block px-4 py-2 text-sm text-gray-700",
        :role "menuitem",
        :tabindex "-1",
        :id "mobile-menu-item-1"}
       "View"]]]]])

(defn content [session-id]
  [:div
   {:class "min-h-full"}
   [:nav
    {:class "bg-gray-800"}
    [:div
     {:class "mx-auto max-w-7xl px-4 sm:px-6 lg:px-8"}
     [:div
      {:class "flex h-16 items-center justify-between"}
      [:div
       {:class "flex items-center"}
       [:div
        {:class "hidden md:block"}
        [:div
         {:class "ml-10 flex items-baseline space-x-4"}
         (comment
           "Current: \"bg-gray-900 text-white\", Default: \"text-gray-300 hover:bg-gray-700 hover:text-white\"")
         [:a
          {:href "/dashboard",
           :class
           "rounded-md bg-gray-900 px-3 py-2 text-sm font-medium text-white"}
          "Dashboard"]
         [:div
          {:class
           "rounded-md px-3 py-2 text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white"
           :aria-current "page"}
          "Products"]
         [:a
          {:href "#",
           :class
           "rounded-md px-3 py-2 text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white"}
          "Projects"]
         [:a
          {:href "#",
           :class
           "rounded-md px-3 py-2 text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white"}
          "Calendar"]
         [:a
          {:href "#",
           :class
           "rounded-md px-3 py-2 text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white"}
          "Reports"]]]]
      [:div
       {:class "hidden md:block"}
       [:div
        {:class "ml-4 flex items-center md:ml-6"}
        [:button
         {:type "button",
          :class
          "relative rounded-full bg-gray-800 p-1 text-gray-400 hover:text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800"}
         [:span {:class "absolute -inset-1.5"}]
         [:span {:class "sr-only"} "View notifications"]
         [:svg
          {:class "h-6 w-6",
           :fill "none",
           :viewBox "0 0 24 24",
           :stroke-width "1.5",
           :stroke "currentColor",
           :aria-hidden "true"}
          [:path
           {:stroke-linecap "round",
            :stroke-linejoin "round",
            :d
            "M14.857 17.082a23.848 23.848 0 005.454-1.31A8.967 8.967 0 0118 9.75v-.7V9A6 6 0 006 9v.75a8.967 8.967 0 01-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 01-5.714 0m5.714 0a3 3 0 11-5.714 0"}]]]
        (comment "Profile dropdown")
        [:div
         {:class "relative ml-3"}
         [:div
          [:button
           {:type "button",
            :class
            "relative flex max-w-xs items-center rounded-full bg-gray-800 text-sm focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800",
            :id "user-menu-button",
            :aria-expanded "false",
            :aria-haspopup "true"}
           [:span {:class "absolute -inset-1.5"}]
           [:span {:class "sr-only"} "Open user menu"]
           [:img
            {:class "h-8 w-8 rounded-full",
             :src
             "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
             :alt ""}]]]]]]
      [:div
       {:class "-mr-2 flex md:hidden"}
       (comment "Mobile menu button")
       [:button
        {:type "button",
         :class
         "relative inline-flex items-center justify-center rounded-md bg-gray-800 p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800",
         :aria-controls "mobile-menu",
         :aria-expanded "false"}
        [:span {:class "absolute -inset-0.5"}]
        [:span {:class "sr-only"} "Open main menu"]
        (comment "Menu open: \"hidden\", Menu closed: \"block\"")
        [:svg
         {:class "block h-6 w-6",
          :fill "none",
          :viewBox "0 0 24 24",
          :stroke-width "1.5",
          :stroke "currentColor",
          :aria-hidden "true"}
         [:path
          {:stroke-linecap "round",
           :stroke-linejoin "round",
           :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]
        (comment "Menu open: \"block\", Menu closed: \"hidden\"")
        [:svg
         {:class "hidden h-6 w-6",
          :fill "none",
          :viewBox "0 0 24 24",
          :stroke-width "1.5",
          :stroke "currentColor",
          :aria-hidden "true"}
         [:path
          {:stroke-linecap "round",
           :stroke-linejoin "round",
           :d "M6 18L18 6M6 6l12 12"}]]]]]]
    (comment "Mobile menu, show/hide based on menu state.")
    [:div
     {:class "md:hidden", :id "mobile-menu"}
     [:div
      {:class "space-y-1 px-2 pb-3 pt-2 sm:px-3"}
      (comment
        "Current: \"bg-gray-900 text-white\", Default: \"text-gray-300 hover:bg-gray-700 hover:text-white\"")
      [:a
       {:href "#",
        :class
        "block rounded-md bg-gray-900 px-3 py-2 text-base font-medium text-white",
        :aria-current "page"}
       "Dashboard"]
      [:a
       {:href "#",
        :class
        "block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"}
       "Team"]
      [:a
       {:href "#",
        :class
        "block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"}
       "Projects"]
      [:a
       {:href "#",
        :class
        "block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"}
       "Calendar"]
      [:a
       {:href "#",
        :class
        "block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"}
       "Reports"]]
     [:div
      {:class "border-t border-gray-700 pb-3 pt-4"}
      [:div
       {:class "flex items-center px-5"}
       [:div
        {:class "flex-shrink-0"}
        [:img
         {:class "h-10 w-10 rounded-full",
          :src
          "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
          :alt ""}]]
       [:div
        {:class "ml-3"}
        [:div
         {:class "text-base font-medium leading-none text-white"}
         "Tom Cook"]
        [:div
         {:class "text-sm font-medium leading-none text-gray-400"}
         "tom@example.com"]]
       [:button
        {:type "button",
         :class
         "relative ml-auto flex-shrink-0 rounded-full bg-gray-800 p-1 text-gray-400 hover:text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800"}
        [:span {:class "absolute -inset-1.5"}]
        [:span {:class "sr-only"} "View notifications"]
        [:img
         {:class "h-8 w-8 rounded-full",
          :src
          "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAACXBIWXMAAAsTAAALEwEAmpwYAAAE3klEQVR4nO2a24vVVRTHP+OoM17IGZ0ms9LulJfpgn9CkQpTGr2UBUGUPdVMPfQQpBARZi/VUPRST5HUS2Z3JEaILK28PNSkU1FeKKx5CJpRx/HEgu+GxZk5v9/e+5wzSfiFwznwW9+192/vtdZea+0DF/D/RQdwJ/ACsBP4ATgJjOljv78H3pdML7CA8wRtwEbgE+AsUEn8GOdj4D7pmna0A08Ax92kTgGDwGZgA7ACWCTZdv1eCdwtmd3Aacc/BvRJdlqwFhh2E/gW2AR0ZujqFPc7p+8IcAdNhK3Uq25AG3xNg3S3aIEOOP2vNMPcLgG+0QCjwONAa6MHAWYC/QoONtZeoLtRyq/UdpviIWBVifx1wDPALuCEfOcfRbGXgGURY94EHNaYhzWHuncivMRXctgie38TmCiJVH8DDwAXlYzdBXztXqa7Hp8I5rQHmF8ge6lWPEz0Ddn8DcBcfSxiPe/C9Jh2yJ7Vwnz3MntzfeY1Z06LShx10EWwpSV6H9S5c0acL0om2OXMbCD1JdY5xy7zibske7LkhauxUmeHcZ8qkb3ZBYDo0DwH+EmkxyLk35KsHZCp6BX31wjZfucvUSb2pDsnYkLsX5K/hnS0aCeNvyQiNB+UrIX/Ugc/nrCF85yD5+Jz6bg9weSPle3K/RK0aBWDKyR/lHzslA4zs5gdDOnMvUWCn0no4chJdLsVykUYMzbdeVTyHxUdaGd1ElttEYNWHYBntFo5COdPT6T8QmXN47XqmfVSaDabgt/Eu4x0zNMinFa0jMXuInN8UQ8tT0rBe+LZeZKK28T9MpG3RTyrNCfhg8wJPSeefafiWXEtVUnBBvGsbJ6EkAbcmKBwhiuyniYdYWWHpSsWK1z6NAl/6mFKmrFEnBHyMSIdlnjGosulRZMQ6ufZCQpnKx+bSIh01ZFyQjpmJfDaXJ+gIS+CuiAV+djqBN5q55c1z4ScFwmmZXE6BbcAf4i7NYG3VZzfpSMFhaYVnN2KoVTcKq41D2IREsDUlzAsL3L23PCLIs5R8a0yLMNal77nZATrxbc8rWEHYkCf+D+XOH6HZCri5GBz0YGYm6IEzHL19ZRbLgy5GjwlUnkMFqUoOUljrbTeQmothC6LyeZgYVnS6FPqRzIHmSv+uQKZc5Ip6p4UYVNMyA6FlRUvOVjgWj21cEoyOVcKLa6wsg5+Yal7IrHQ8bhWXDuTaiGcVyabinWxpa5vPhxQwR+Ly9WNrCi1r4UdrulnnFjMBA7FNh+q20Ex4XGVGmejLvwWOfJSF35Hxe1pRjvIb+FYjUG6tSr7XE/XotH2yBTHZN6u6hPv12QX12hqJzfoqlumPyq3CR2/7Qp9YQIjWlWrEVJhnJddb6yiI+BdpT1o7CO5LdOpmtgDbgXHVZ3d06DLmDZdye1wizShi6U99TaxDRdrR7z5vANcT/OwDHi9ateHdcVRF652zv9LQtumHvS4gGAvcVWjFC/WlUFFt0/9Tbp6a1UzfNSZU907MZXPhAAQTv9G3r5aar/f6R9o9t37VNfTD2Ummh3iVl9Pr2GaMEcmENKZijqGu9Ti6VVbqVNF1wz9Xq5nWyQbbq0qugXom84/DHi0K9H8tM6/cGz8r/7CUctMrEzeBnyoAmpEYXRcv4f0bJtkc+ueC+B8x78fHcKd4U6+1gAAAABJRU5ErkJggg==",
          :alt ""}]]]
      [:div
       {:class "mt-3 space-y-1 px-2" :style "display: none;"}
       [:a
        {:href "#",
         :class
         "block rounded-md px-3 py-2 text-base font-medium text-gray-400 hover:bg-gray-700 hover:text-white"}
        "Your Profile"]
       [:a
        {:href "#",
         :class
         "block rounded-md px-3 py-2 text-base font-medium text-gray-400 hover:bg-gray-700 hover:text-white"}
        "Settings"]
       [:a
        {:href "#",
         :class
         "block rounded-md px-3 py-2 text-base font-medium text-gray-400 hover:bg-gray-700 hover:text-white"}
        "Sign out"]]]]]
   [:header
    {:class "bg-white shadow"}
    [:div
     {:class "mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8"}
     [:h1
      {:class "text-3xl font-bold tracking-tight text-gray-900"}
      "Produkte List"]]]
   [:main
    [:div
     {:class "mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8"}
     (map product-data (p/get-session-products (keyword session-id)))]]])

(defn product-selected [session-id product-id]
  (p/select-product session-id product-id)
  [:div {:class "flex justify-center items-center h-9"}
   [:svg
    {:class "-ml-0.5 mr-1.5 h-5 w-5",
     :viewBox "0 0 20 20",
     :fill "currentColor",
     :aria-hidden "true"}
    [:path
     {:fill-rule "evenodd",
      :d
      "M16.704 4.153a.75.75 0 01.143 1.052l-8 10.5a.75.75 0 01-1.127.075l-4.5-4.5a.75.75 0 011.06-1.06l3.894 3.893 7.48-9.817a.75.75 0 011.05-.143z",
      :clip-rule "evenodd"}]]])