(ns app.html.core
	(:require [hiccup.page :refer [html5 include-js include-css]]))

(defn index-page [] 
  (html5 
   [:head
    [:title "Invoice Generator"]
    [:link {:href "tailwind.min.css" :rel "stylesheet"}]
    [:script {:src "htmx.min.js"}]] 
  	[:body
   	 [:section
      [:div.mx-auto.max-w-screen-2xl.px-4.py-8.sm:px-6.lg:px-8
       [:h1 "Generate Invoice"]
       [:form.mt-8.grid.grid-cols-6.gap-6 {:hx-post "/generate-invoice" :hx-target "this" :hx-swap "outerHTML"}
        [:div
         [:label.block.text-sm.font-medium.text-gray-700.dark:text-gray-200 "Invoice Number:"]
         [:input#invoice-number.block.w-full.rounded-md.border-0.py-1.5.text-gray-900.shadow-sm.ring-1.ring-inset.ring-gray-300.placeholder:text-gray-400.focus:ring-2.focus:ring-inset.focus:ring-indigo-600.sm:text-sm.sm:leading-6 {:type "text" :name "invoice-number"}]]
        [:div
         [:label.block.text-sm.font-medium.text-gray-700.dark:text-gray-200 "Invoice Date:"]
         [:input#invoice-date.block.w-full.rounded-md.border-0.py-1.5.text-gray-900.shadow-sm.ring-1.ring-inset.ring-gray-300.placeholder:text-gray-400.focus:ring-2.focus:ring-inset.focus:ring-indigo-600.sm:text-sm.sm:leading-6 {:type "date" :name "invoice-date"}]]
       				 ;; Add more fields as needed
        [:input.inline-block.shrink-0.rounded-md.border.border-blue-600.bg-blue-600.px-12.py-3.text-sm.font-medium.text-white.transition.hover:bg-transparent.hover:text-blue-600.focus:outline-none.focus:ring.active:text-blue-500.dark:hover:bg-blue-700.dark:hover:text-white {:type "submit" :value "Generate Invoice"}]]]]]))