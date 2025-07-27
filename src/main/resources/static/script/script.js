console.log("Script loaded");

// Immediately apply theme before DOMContentLoaded
let currentTheme = getTheme();
console.log("Current theme from localStorage:", currentTheme);
document.documentElement.classList.add(currentTheme);

// Update button label based on current theme
document.addEventListener("DOMContentLoaded", () => {
    const toggleBtn = document.querySelector("#theme_changer");
    const html = document.documentElement;

    if (toggleBtn) {
        // Set initial button text
        toggleBtn.textContent = html.classList.contains("dark") ? "Light Mode" : "Dark Mode";

        // Toggle logic
        toggleBtn.addEventListener("click", () => {
            if (html.classList.contains("dark")) {
                html.classList.remove("dark");
                localStorage.setItem("theme", "light");
                toggleBtn.textContent = "Dark Mode";
            } else {
                html.classList.add("dark");
                localStorage.setItem("theme", "dark");
                toggleBtn.textContent = "Light Mode";
            }
        });
    }

    const currentTheme = localStorage.getItem("theme") || "light";
const themeLabel = document.getElementById("theme-label");

if (themeLabel) {
  themeLabel.textContent = currentTheme === "dark" ? "Light" : "Dark";
}
});

// Helper function
function getTheme() {
    const theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}



 document.addEventListener("DOMContentLoaded", function () {
    const showBtn = document.querySelector('[data-drawer-show]');
    const hideBtn = document.querySelector('[data-drawer-hide]');
    const mainContent = document.getElementById("mainContent");

    showBtn?.addEventListener("click", () => {
      mainContent?.classList.add("ml-64");
    });

    hideBtn?.addEventListener("click", () => {
      mainContent?.classList.remove("ml-64");
    });
  });
document.addEventListener("DOMContentLoaded", function () {
  const toggleBtn = document.getElementById("mobileMenuToggle");
  const menu = document.getElementById("navbar-cta");

  if (toggleBtn && menu) {
    // Toggle menu visibility
    toggleBtn.addEventListener("click", function () {
      menu.classList.toggle("hidden");
    });

    // Hide dropdown if clicked outside
    document.addEventListener("click", function (e) {
      if (!menu.contains(e.target) && !toggleBtn.contains(e.target)) {
        menu.classList.add("hidden");
      }
    });
  }

});

