// import React, { useState } from 'react';
// import './LoginPage.css';

// const LoginPage = () => {
//   const [username, setUsername] = useState('');

//   const handleUsernameChange = (event) => {
//     setUsername(event.target.value);
//   };


//   const handleSubmit = (event) => {
//     event.preventDefault();
//     // Add your login logic here
//     console.log('Username:', username);
//   };

//   return (
//     <div className="login-container">
//       <h2 className="login-title">Login</h2>
//       <form className="login-form" onSubmit={handleSubmit}>
//         <div className="form-group">
//           <label className="form-label">Username:</label>
//           <input
//             className="form-input"
//             type="text"
//             value={username}
//             onChange={handleUsernameChange}
//           />
//         </div>
//         <div className="form-group">
//           <button className="login-button" type="submit">Login</button>
//         </div>
//       </form>
//     </div>
//   );
// };

// export default LoginPage;

// LoginPage.js
import React, { useContext, useState } from 'react';
import MyContext from './MyContext';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
  const [inputLogin, setInputLogin] = useState('');
  const { updateLogin } = useContext(MyContext);
  const navigate = useNavigate();

  const handleInputChange = (event) => {
    setInputLogin(event.target.value);
  };

  const handleFormSubmit = (event) => {
    event.preventDefault();
    updateLogin(inputLogin);
    navigate('/home');
  };

  return (
    <div>
      <h1>Login Page</h1>
      <form onSubmit={handleFormSubmit}>
        <input
          type="text"
          value={inputLogin}
          onChange={handleInputChange}
        />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

export default LoginPage;




