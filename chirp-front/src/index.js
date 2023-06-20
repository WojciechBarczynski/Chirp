import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

// import React from 'react';
// import ReactDOM from 'react-dom';
// import './index.css';
// import App from './App';
// import { LoginProvider } from './LoginService';

// ReactDOM.render(
//   <React.StrictMode>
//     <LoginProvider>
//       <App />
//     </LoginProvider>
//   </React.StrictMode>,
//   document.getElementById('root')
// );
