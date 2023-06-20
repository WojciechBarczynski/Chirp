// MyContext.js
import React, { createContext, useState } from 'react';

const MyContext = createContext();

export const MyContextProvider = ({ children }) => {
  const [login, setLogin] = useState('');

  const updateLogin = (newLogin) => {
    setLogin(newLogin);
  };

  return (
    <MyContext.Provider value={{ login, updateLogin }}>
      {children}
    </MyContext.Provider>
  );
};

export default MyContext;
