// footer.js

// Render the footer content into the page
function renderFooter() {

  // Select the footer element from the DOM
  const footer = document.getElementById("footer");

  // Set the inner HTML of the footer element
  footer.innerHTML = `

    <!-- 2. Footer Wrapper -->
    <footer class="footer">

      <!-- 3. Footer Container -->
      <div class="footer-container">

        <!-- 4. Hospital Logo and Copyright Info -->
        <div class="footer-logo">
          <img src="../assets/images/logo/logo.png" alt="Hospital CMS Logo">
          <p>© Copyright 2025. All Rights Reserved by Hospital CMS.</p>
        </div>

        <!-- 5. Links Section -->
        <div class="footer-links">

          <!-- 6. Company Column -->
          <div class="footer-column">
            <h4>Company</h4>
            <a href="#">About</a>
            <a href="#">Careers</a>
            <a href="#">Press</a>
          </div>

          <!-- 7. Support Column -->
          <div class="footer-column">
            <h4>Support</h4>
            <a href="#">Account</a>
            <a href="#">Help Center</a>
            <a href="#">Contact Us</a>
          </div>

          <!-- 8. Legals Column -->
          <div class="footer-column">
            <h4>Legals</h4>
            <a href="#">Terms & Conditions</a>
            <a href="#">Privacy Policy</a>
            <a href="#">Licensing</a>
          </div>

        </div><!-- end .footer-links -->

      </div><!-- end .footer-container -->

    </footer>
  `;
}

// Call renderFooter to populate the footer on page load
renderFooter();