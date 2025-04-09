document.getElementById('registerForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const id = document.getElementById('username').value;
    const full_name = document.getElementById('user_full_name').value;
    const email = document.getElementById('user_email').value;
    const phone = document.getElementById('user_phone').value;
    const profile = document.getElementById('user_profile').value;
    const password = document.getElementById('password').value;
    const confirmation = document.getElementById('confirmation').value;
    let cc_bi = document.getElementById('user_cc_bi').value;
    let role = document.getElementById('user_role').value;
    let nif = document.getElementById('user_nif').value;
    let company = document.getElementById('user_company').value;
    let job = document.getElementById('user_job').value;
    let address = document.getElementById('user_address').value;
    let company_nif = document.getElementById('user_company_nif').value;
    let account_state = document.getElementById('user_account_state').value;

    if(cc_bi === null) cc_bi = "";
    if(role === null) role = "enduser";
    if(nif === null) nif = "";
    if(company === null) company = "";
    if(job === null) job = "";
    if(address === null) address = "";
    if(company_nif === null) company_nif = "";
    if(account_state === null) account_state = "inactive";

    fetch('/rest/register/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id, full_name, email, phone, profile, password, confirmation,
                                     cc_bi, role, nif, company, job, address, company_nif, account_state })
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