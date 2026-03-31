# 🌌 Solar System Simulation

> *A real-time animated solar system built entirely in Java Swing — no game engines, no external libraries, just pure Java.*

<br/>

[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-5382A1?style=for-the-badge)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)
[![Stars](https://img.shields.io/github/stars/Monojit-Pal/Solar-System-Simulation?style=for-the-badge&color=yellow)](https://github.com/Monojit-Pal/Solar-System-Simulation/stargazers)
[![Forks](https://img.shields.io/github/forks/Monojit-Pal/Solar-System-Simulation?style=for-the-badge)](https://github.com/Monojit-Pal/Solar-System-Simulation/forks)

<br/>

</div>

---

## ✨ Features

- 🌞 **Radiant Sun** — rendered with a warm radial gradient (yellow-orange glow)
- 🪐 **All 8 Planets** — Mercury through Neptune, each with individually tuned orbit radius, size, speed, and color
- 💍 **Saturn's Ring** — drawn with a rotated ellipse using `AffineTransform` for a 3D tilt effect
- ⭐ **Starfield Background** — 220 randomly seeded stars of varying sizes fill the dark cosmos
- 🔄 **Smooth 60 FPS Animation** — powered by a Swing `Timer` firing every ~16ms
- 🏷️ **Planet Name Labels** — rendered dynamically beside each moving planet
- 🎨 **Anti-aliased Rendering** — crisp edges via `RenderingHints.VALUE_ANTIALIAS_ON`

---

## 🔭 How It Works — Logic, Physics & Design

### 🌀 Orbital Motion (Circular Orbit Model)

Each planet follows a **circular orbit** around the sun. The position at any point in time is computed using standard **parametric circle equations**:

```
x = cx + r · cos(θ)
y = cy + r · sin(θ)
```

Where:
- `cx`, `cy` → center of the sun (fixed point on the canvas)
- `r` → orbital radius (distance from sun, in pixels)
- `θ` (theta) → current angle (in radians), incremented each frame

Every frame, `θ` is updated by the planet's `speed` value:

```java
void tick() {
    angle += speed;  // θ += Δθ per frame
}
```

> **Real-world analogy:** This mirrors Kepler's First Law — planets orbit the sun. Kepler's Third Law (farther planets move slower) is approximated here: Mercury has `speed = 0.055` while Neptune has `speed = 0.012`. Real orbital periods aren't exact, but the *relative* feel is physically intuitive.

---

### ⏱️ The Animation Loop

```java
Timer timer = new Timer(16, e -> {
    for (Planet p : planets) p.tick();
    repaint();
});
timer.start();
```

A `javax.swing.Timer` fires every **16 milliseconds** (~62.5 FPS), calling `tick()` on every planet to advance their angles, then calling `repaint()` to redraw the panel. This is the **game loop** pattern — update state, then render.

---

### 🎨 Rendering Pipeline (inside `paintComponent`)

Each frame, the following draw order is followed:

```
Background fill → Stars → Orbit rings → Sun (gradient) → Planets → Saturn's ring → Labels
```

Painter's algorithm — things drawn first are "behind". Stars behind orbits, orbits behind planets, etc.

**Sun gradient:**
```java
GradientPaint sun = new GradientPaint(
    center.x - sunSize, center.y - sunSize, new Color(255, 220, 120),  // bright yellow
    center.x + sunSize, center.y + sunSize, new Color(255, 140, 50)    // deep orange
);
g2.setPaint(sun);
g2.fillOval(...);
```

A `GradientPaint` flows from bright yellow (top-left) to deep orange (bottom-right), giving the sun a lit, three-dimensional appearance.

---

### 💍 Saturn's Ring — `AffineTransform` in Action

Saturn's ring is drawn as an ellipse rotated around Saturn's current position:

```java
AffineTransform old = g2.getTransform();  // save current transform
g2.translate(px, py);                     // move origin to Saturn's center
g2.rotate(-0.6);                          // tilt the ring ~34°
g2.drawOval(-p.size - 6, -p.size / 2, (p.size + 6) * 2, p.size);
g2.setTransform(old);                     // restore original transform
```

The key here is **save → modify → draw → restore**. Without restoring the old transform, every subsequent planet would also be rotated — a classic graphics bug.

---

### ⭐ Starfield Generation

Stars are generated **once at startup** using a seeded `Random(42)`:

```java
Random rng = new Random(42);  // seed = 42 → same stars every run (deterministic)
s[i][0] = rng.nextInt(w);     // x
s[i][1] = rng.nextInt(h);     // y
s[i][2] = 1 + rng.nextInt(2); // size: 1 or 2 pixels
```

Using a fixed seed (`42`) means the starfield is always identical — no flickering or randomness between sessions.

---

### 🏷️ Dynamic Label Positioning

Planet labels are nudged outward from the sun so they don't overlap the planet:

```java
int lx = px + (px - center.x) / Math.max(1, (int)(p.r / 10.0)) + 8;
int ly = py + (py - center.y) / Math.max(1, (int)(p.r / 10.0));
```

`(px - center.x)` gives the direction vector from sun to planet. Dividing by `r/10` normalizes it into a small offset, pushing the label slightly away from the sun center. The `+8` adds a fixed pixel gap.

---

## 🪐 Planet Data

| Planet  | Orbit Radius | Size | Speed   | Color              |
|---------|:------------:|:----:|:-------:|:------------------:|
| Mercury | 70 px        | 6 px | 0.055   | Dark grey          |
| Venus   | 110 px       | 10 px| 0.040   | Pale yellow        |
| Earth   | 150 px       | 10 px| 0.035   | Blue               |
| Mars    | 185 px       | 8 px | 0.030   | Reddish            |
| Jupiter | 250 px       | 18 px| 0.020   | Tan/beige          |
| Saturn  | 310 px       | 16 px| 0.017   | Pale gold + ring   |
| Uranus  | 360 px       | 12 px| 0.014   | Cyan               |
| Neptune | 410 px       | 12 px| 0.012   | Deep blue          |

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 8 or higher** — [Download here](https://adoptium.net/)
- A terminal (Command Prompt, PowerShell, Terminal, bash — anything works)

---

### Step 1 — Fork the Repository

Click the **Fork** button at the top-right of this page to create your own copy of the repository under your GitHub account.

---

### Step 2 — Clone Your Fork

```bash
git clone https://github.com/<your-username>/Solar-System-Simulation.git
```

Then navigate into the project folder:

```bash
cd Solar-System-Simulation
```

---

### Step 3 — Compile with `javac`

```bash
javac SolarSystem.java
```

This compiles the source file and generates `SolarSystem.class` in the same directory.

> ✅ No build tools, no Maven, no Gradle — just one command.

---

### Step 4 — Run with `java`

```bash
java SolarSystem
```

A window titled **"Solar System Simulation"** will open and the animation will start immediately. 🎉

---

## 📁 Repository Structure

```
Solar-System-Simulation/
│
├── assets/
│   ├── solar-system-simulation-screenshoot.png   # Preview screenshot
│   └── solar-system-simulation-demo.mp4          # Animated demo video
│
├── SolarSystem.java    # The entire simulation — one file, zero dependencies
├── LICENSE             # MIT License
└── README.md           # You are here 👋
```

---

## 🛠️ Possible Improvements

Want to contribute or experiment? Here are some ideas:

- [ ] 🌙 **Add moons** — Earth's Moon, Jupiter's Galilean moons (Io, Europa, Ganymede, Callisto)
- [ ] ☄️ **Add a comet** with a highly elliptical orbit (use actual ellipse math instead of circle)
- [ ] 🌗 **Planet shading** — add a dark-side shadow using a radial gradient or arc clip
- [ ] 🔢 **Speed controls** — add a JSlider to control simulation speed in real time
- [ ] 📊 **Info panel** — click a planet to show its name, orbital period, and distance from sun
- [ ] 🎯 **Accurate scaling** — implement real relative planet sizes and distances (with zoom)
- [ ] 💫 **Asteroid belt** — render a particle ring between Mars and Jupiter
- [ ] 🌈 **Planet textures** — load image sprites for each planet instead of flat-fill ovals
- [ ] 📸 **Screenshot export** — press `S` to save the current frame as a PNG

---

## 🤝 Contributing

Contributions are welcome! If you have an idea, find a bug, or want to add a feature:

1. Fork the repository
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "Add: your feature description"`
4. Push the branch: `git push origin feature/your-feature-name`
5. Open a **Pull Request** — describe what you changed and why

Please keep code clean and commented, especially for physics or math logic.

---

## ⭐ Show Some Love

If you found this project interesting, educational, or just plain cool —  
**please leave a ⭐ star on the repository!**

It takes one click and means a lot. It also helps others discover the project.

> *"The cosmos is within us. We are made of star-stuff."* — Carl Sagan

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.  
Free to use, modify, and share. Attribution appreciated. 🙏
