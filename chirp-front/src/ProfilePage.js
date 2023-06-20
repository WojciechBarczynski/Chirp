import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './ProfilePage.css'

const ProfilePage = () => {
  const [username, setUsername] = useState('JohnDoe');
  const [email, setEmail] = useState('johndoe@example.com');
  const [bio, setBio] = useState('Hello, I am John Doe.');

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handleBioChange = (e) => {
    setBio(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Perform submission logic here
    console.log('Changes submitted:', { username, email, bio });
  };

  const handleCancel = () => {
    // Reset the form fields or navigate back
    setUsername('JohnDoe');
    setEmail('johndoe@example.com');
    setBio('Hello, I am John Doe.');
  };

  return (
    <div className="profile-page">
      <div className="profile-photo">
        {/* Circular profile photo */}
        <img src="profile-photo.jpg" alt="Profile" className="photo" />
      </div>
      <div className="account-data">
        {/* Account data form */}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input type="text" id="username" value={username} onChange={handleUsernameChange} />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input type="email" id="email" value={email} onChange={handleEmailChange} />
          </div>
          <div className="form-group">
            <label htmlFor="bio">Bio:</label>
            <textarea id="bio" value={bio} onChange={handleBioChange} />
          </div>
          <div className="form-buttons">
            <button type="submit">Submit</button>
            <Link to="/">
              <button type="button" onClick={handleCancel}>Cancel</button>
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProfilePage;
