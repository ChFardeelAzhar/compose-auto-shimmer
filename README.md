# ✨ ComposeAutoShimmer

**ComposeAutoShimmer** is a professional-grade Jetpack Compose library designed to solve the "Skeleton Screen" problem once and for all. It eliminates the need for manual measurements, hardcoded Dp values, and "eyeballing" your loading states.

---

## 🚀 Why ComposeAutoShimmer?

Building skeleton screens by hand is a massive time sink. Every time your design changes, you have to rebuild your skeleton. 

**ComposeAutoShimmer** handles this automatically. You wrap your real UI, and the library creates a pixel-perfect shimmer silhouette of your components.

### 🌟 Key Features
- **Auto-Silhouette**: No need to define widths or heights. It masks your real UI to create a skeleton.
- **Single Wrapper Entry**: Just wrap `ShimmerBox { YourUI() }` and toggle `isLoading`.
- **Global Themes**: Configure colors, duration, and angles once via `ShimmerTheme`.
- **Lightweight & Native**: Built entirely on the Jetpack Compose graphics pipeline for 60fps performance.
- **Dark Mode Ready**: Automatic support for dark/light shimmer palettes.

---

## 📦 Core Components

### 1. `ShimmerBox` (The Magic Wrapper)
Wrap any complex UI (User Cards, Feed Items, Lists) and it will automatically generate a skeleton of the internal components.

```kotlin
ShimmerBox(isLoading = true) {
    UserCardUI() // This entire UI will automatically become a shimmer skeleton
}
```

### 2. `ShimmerOverlay` (Additive Sweep)
Perfect for adding a shimmer "glint" on top of visible images or promotional banners.
```kotlin
ShimmerOverlay(active = true) {
    ProductBanner() // Shimmer sweeps over the visible image
}
```

### 3. `ShimmerPlaceholder` (Leaf Skeleton)
For simple use cases where you want a leaf-level skeleton block (like a single line of text or an avatar circle).
```kotlin
ShimmerPlaceholder(width = 120.dp, height = 16.dp)
```

---

## 🛠 Usage

### 1. Configure Global Defaults
Wrap your app or screen in `ShimmerTheme` to set the look and feel.

```kotlin
val myConfig = ShimmerConfig(
    baseColor = Color.LightGray,
    highlightColor = Color.White,
    durationMillis = 1500
)

ShimmerTheme(config = myConfig) {
    AppContent()
}
```

### 2. Implementation
```kotlin
@Composable
fun ProductCard(isLoading: Boolean) {
    ShimmerBox(isLoading = isLoading) {
        Column {
            Image(...)
            Text("Product Name")
            Text("$99.99")
        }
    }
}
```

---

## 🎨 How it works: The "Super-Auto" Engine
Unlike traditional libraries that require you to draw every grey box manually, **ComposeAutoShimmer** uses a multi-layered approach:

1.  **Material-Aware "X-Ray" Vision**: It automatically detects Material3 components and makes backgrounds transparent during loading. This "reveals" the inner text and icons for the shimmer engine.
2.  **Auto-Silhouette**: It treats your UI as a mask. It measures your layout and draws your UI as a perfect skeleton silhouette.
3.  **Glossy 5-Stop Gradient**: We use a sharp, high-contrast 5-stop gradient sweep to create a professional "glint" effect that looks premium on any screen.

**Result**: A shimmer effect that perfectly matches the text lines, image shapes, and rounded corners of your real UI with zero manual code.

---

## 📄 License
MIT License. Created with ❤️ for the Jetpack Compose Community.
