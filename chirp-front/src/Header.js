import React from 'react';
import { Link } from 'react-router-dom';
import './MainPage.css';

const Header = () => {
  return (
    <header className="menu-header">
      <Link to="/add-post">
        <button className="menu-button">Add</button>
      </Link>
      <div className="search-bar">
        <input type="text" placeholder="Search" />
        <button className="search-button">Search</button>
      </div>
      <Link to="/my-profile">
        <button className="profile-button">Profile</button>
      </Link>
    </header>
  );
};
<Link to="/add-post">
</Link>
export default Header;
