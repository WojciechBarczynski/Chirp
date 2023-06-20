// import React from 'react';
// import LoginPage from './LoginPage';
// import MainPage from './MainPage';
// import AddPostPage from './AddPostPage';
// import ProfilePage from './ProfilePage';
// import Post from './Post';
// import PostPage from './PostPage';

// const App = () => {
//   return (
//     <div className="App">
//       {/* <AddPostPage /> */}
//       {/* <LoginPage/> */}
//       {/* <MainPage/> */}
//       {/* <ProfilePage/> */}
//       <PostPage/>
//     </div>
//   );
// };

// export default App;

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './LoginPage';
import MainPage from './MainPage';
import AddPostPage from './AddPostPage';
import ProfilePage from './ProfilePage';
import Post from './Post';
import PostPage from './PostPage';
import Profile from './Profile';

const App = () => {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/add-post" element={<AddPostPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/my-profile" element={<ProfilePage />} />
          <Route path="/post" element={<Post />} />
          <Route path="/post/:id" element={<PostPage />} />
          <Route path="/profile/" element={<Profile />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;



