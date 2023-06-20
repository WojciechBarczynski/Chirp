// DashboardPage.js
import React, { useContext } from 'react';
import MyContext from './MyContext';

const DashboardPage = () => {
  const { login } = useContext(MyContext);

  return (
    <div>
      <h1>Welcome to the Dashboard!</h1>
      {login ? (
        <p>You are logged in as: {login}</p>
      ) : (
        <p>Please log in to view the dashboard.</p>
      )}
    </div>
  );
};

export default DashboardPage;
