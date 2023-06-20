import React, { useContext, useState } from 'react';
import MyContext from './MyContext';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css'; // Import the CSS file

const LoginPage = () => {
  const [inputLogin, setInputLogin] = useState('');
  const { updateLogin } = useContext(MyContext);
  const navigate = useNavigate();

  const handleInputChange = (event) => {
    setInputLogin(event.target.value);
  };

  const handleFormSubmit = async (event) => {
    event.preventDefault();

    const newUser = {
      userName: `"${inputLogin}"`,
    };

    try {
      const response = await fetch('http://127.0.0.1:8080/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newUser),
      });

      if (response.ok) {
        const data = await response.json();
        console.log(data); // Assuming the server responds with the created user data
        updateLogin(inputLogin);
        navigate('/home');
      } else {
        throw new Error('Failed to create a new user');
      }
    } catch (error) {
      console.error(error);
      // Handle the error appropriately (e.g., show an error message)
    }
  };


  return (
    <div className="login-page">
      <div className="login-box">
        <h1>Login Page</h1>
        <form onSubmit={handleFormSubmit}>
          <div className="form-group">
            <label className="form-label">Username</label>
            <input
              type="text"
              value={inputLogin}
              onChange={handleInputChange}
              className="form-input"
            />
          </div>
          <button type="submit" className="submit-button">Submit</button>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;