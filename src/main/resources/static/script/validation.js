function validateContactForm() {
  const name = document.getElementById("contact-name");
  const email = document.getElementById("contact-email");
  const phone = document.getElementById("contact-phone");
  const address = document.getElementById("address");

  const errorName = document.getElementById("error-name");
  const errorEmail = document.getElementById("error-email");
  const errorPhone = document.getElementById("error-phone");
  const errorAddress = document.getElementById("error-address");

  let isValid = true;

  // Reset previous errors
  [name, email, phone, address].forEach(input => input.classList.remove("border-red-500"));
  [errorName, errorEmail, errorPhone, errorAddress].forEach(span => span.innerText = "");

  // Validate name
  if (name.value.trim() === "") {
    errorName.innerText = "Name is required.";
    name.classList.add("border-red-500");
    isValid = false;
  }

  // Validate email
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (email.value.trim() === "") {
    errorEmail.innerText = "Email is required.";
    email.classList.add("border-red-500");
    isValid = false;
  } else if (!emailRegex.test(email.value.trim())) {
    errorEmail.innerText = "Invalid email format.";
    email.classList.add("border-red-500");
    isValid = false;
  }

  // Validate phone
  const phoneRegex = /^\d{10}$/;
  if (phone.value.trim() === "") {
    errorPhone.innerText = "Phone number is required.";
    phone.classList.add("border-red-500");
    isValid = false;
  } else if (!phoneRegex.test(phone.value.trim())) {
    errorPhone.innerText = "Phone number must be exactly 10 digits.";
    phone.classList.add("border-red-500");
    isValid = false;
  }

  // Validate address
  if (address.value.trim() === "") {
    errorAddress.innerText = "Address is required.";
    address.classList.add("border-red-500");
    isValid = false;
  }
  // To hide in 3 sec
  document.addEventListener("DOMContentLoaded", () => {
      const successBox = document.querySelector('[th\\:if="${success}"]');
      if (successBox) {
        setTimeout(() => {
          successBox.style.display = 'none';
        }, 3000); // Hide after 3 seconds
      }
    });
  return isValid;
}