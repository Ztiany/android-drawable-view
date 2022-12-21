# Lib Shape/Drawable View

An android library for developers to avoid writing drawable XML files.

That would be very convenient if we can define various drawables by view's attributes. And that is
what this library can do.

## 1 Lib ShapeView

The Lib-ShapeView harnesses the ability
of [MaterialShapeDrawable](https://developer.android.com/reference/com/google/android/material/shape/MaterialShapeDrawable)
.

We can use views in Lib-ShapeView to define their background directly by attributes like the picture
below shown.

![](shape-view.jpg)

## 2 Lib DrawableView

But if we need a gradient drawable as a view's background. We then need views in The
Lib-DrawableView.

![](drawable-view.jpg)

Acknowledgment: The core code of Lib DrawableView is copied from [又一个减少冗余 Drawable 资源的解决方案](https://mp.weixin.qq.com/s/qxMoI7UTw3WtiRR6oIDGKA)。

## 3  Installation

```groovy
implementation 'io.github.ztiany:android-drawable-view:1.1.0'
```
