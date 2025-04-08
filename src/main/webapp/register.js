document.getElementById('registerForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const id = document.getElementById('username').value;
    const full_name = document.getElementById('user_full_name').value;
    const email = document.getElementById('user_email').value;
    const phone = document.getElementById('user_phone').value;
    const profile = document.getElementById('user_profile').value;
    const password = document.getElementById('password').value;
    const confirmation = document.getElementById('confirmation').value;

    fetch('/rest/register/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id, full_name, email, phone, profile, password, confirmation })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Invalid registration credentials.");
            }
            return response.json();
        })
        .then(token => {
            document.getElementById('registerSuccess').textContent = "User registered successfully.";
        })
        .catch(err => {
            document.getElementById('registerError').textContent = err.message;
        });
});