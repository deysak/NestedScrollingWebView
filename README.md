# NestedScrollingWebView
Nested Scrolling support for Android WebView

---

`NestedScrollingWebView` *is-a* [<ins>`WebView`</ins>](https://developer.android.com/reference/android/webkit/WebView) that implements [<ins>`NestedScrollingChild`</ins>](https://developer.android.com/reference/androidx/core/view/NestedScrollingChild).

It is useful for purposes where [<ins>`WebView`</ins>](https://developer.android.com/reference/android/webkit/WebView) is required to be:
1. Scrollable
2. Zoomable
3. Supporting nested scrolling

**Example:** Hiding the toolbar upon scroll ([Issue-14991](https://github.com/ankidroid/Anki-Android/pull/17250)).

**Why:**
There is no default way to reliably support all the 3 aforementioned behaviors as:
* Using [<ins>`NestedScrollView`</ins>](https://developer.android.com/reference/androidx/core/widget/NestedScrollView) interferes with the zoom
  ([Issue-16135](https://github.com/ankidroid/Anki-Android/issues/16135)).
* Intercepting the scale motion events from [<ins>`NestedScrollView`</ins>](https://developer.android.com/reference/androidx/core/widget/NestedScrollView)
  and passing it to [<ins>`WebView`</ins>](https://developer.android.com/reference/android/webkit/WebView) is unreliable.
* Hiding the toolbar by detecting scroll is unreliable.
* Workarounds for ensuring reliable zoom behavior interferes with scrolling.
* Methods allowing consistent scroll and zoom behavior are not reliable for hiding toolbar.

**Usage:** Refer the example app in the repo.
1. `WebView.setNestedScrollingEnabled` must be set to `true`.
   ([Example](https://github.com/deysak/NestedScrollingWebView/blob/b83fc7b1a03dbff45380886ef8cdc2a71c1e44ef/app/src/main/java/com/deysak/nestedscrollingwebview/MainActivity.kt#L17-L23))
2. XML: `<com.deysak.nestedscrollingwebview.NestedScrollingWebView ... />` ([Example](https://github.com/deysak/NestedScrollingWebView/blob/b83fc7b1a03dbff45380886ef8cdc2a71c1e44ef/app/src/main/res/layout/activity_main.xml#L27-L31))

### Video:

**(A) Infinite Scroll after zoom:**

<details><summary>NestedScrollingWebView ✨</summary>

https://github.com/user-attachments/assets/e7003576-9c2f-4211-8953-2344c2b7ecd7


</details> 

<details><summary> Bug 🐞</summary>

https://github.com/user-attachments/assets/6f5db757-2ad1-40fd-8365-b507c792e3b6


</details> 

**(B) Unreliable Zoom:**

<details><summary> NestedScrollingWebView ✨</summary>

https://github.com/user-attachments/assets/053d2991-b16b-49b0-b524-989ad94eeeb3


</details> 
<details><summary> Bug 🐞</summary>
Notice that I am trying to zoom at start, but it isn't zooming in.

https://github.com/user-attachments/assets/32d8cc55-0a73-4fef-8e46-c65113af530c


</details> 

---

So far it has been only tested for the purpose of collapsible toolbar, while allowing for reliable zoom and scroll functionality.