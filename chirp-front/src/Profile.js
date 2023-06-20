import React from 'react';
import './Profile.css'
import './MainPage.css';
import Header from './Header';
import Post from './Post';

const TagsSection = () => {
  return (
    <div className="ads-section">
      <h3>Tag List</h3>
      {/* Tag list content */}
    </div>
  );
};

const FriendListSection = () => {
  return (
    <div className="friend-list-section">
      <h3>Friend List</h3>
      {/* Friend list content */}
    </div>
  );
};

const ContentSection = ({ posts, photo, username, email, bio }) => {
  return (
    <div>
      <div className="profile">
        <img src={photo} alt="Profile" className="profile-photo" />
        <div className="username">{username}</div>
        <div className="email">{email}</div>
        <div className="bio">{bio}</div>
      </div>
      <div className="content-section">
        <h3>Post Section</h3>
        {posts.map((post, index) => (
          <Post
            key={index}
            profilePicture={post.profilePicture}
            name={post.name}
            date={post.date}
            media={post.media}
            text={post.text}
            reactions={post.reactions}
            shareCount={post.shareCount}
            commentCount={post.commentCount}
            tags={post.tags}
          />
        ))}
      </div>
    </div>
  );
};

const Profile = () => {
  const samplePosts = [
    {
      profilePicture: 'https://imgv3.fotor.com/images/gallery/Realistic-Male-Profile-Picture.jpg',
      name: 'John Doe',
      date: 'June 19, 2023',
      media: 'https://ihe24.pl/87777-large_pp/oprawa-profle-bit-plus-bialy-9020-nowodvorski.jpg',
      text: 'Check out this amazing photo!',
      reactions: [
        { emoji: '👍', count: 10 },
        { emoji: '❤️', count: 5 },
        { emoji: '😆', count: 3 },
        { emoji: '😮', count: 2 },
        { emoji: '😢', count: 1 },
        { emoji: '😡', count: 0 },
      ],
      shareCount: 11,
      commentCount: 50,
      tags: ["tag1", "tag2", "tag3"]
    },
    {
      profilePicture: 'https://jwalcher.com/wp-content/uploads/2016/12/Website-Profle-Pic.png',
      name: 'Jane Smith',
      date: 'June 20, 2023',
      media: 'https://image.ceneostatic.pl/data/products/56320714/i-edimax-en-9320tx-e-low-profle-en9320txelowprofile.jpg',
      text: 'Feeling grateful for the beautiful weather today! ☀️',
      reactions: [
        { emoji: '👍', count: 10 },
        { emoji: '❤️', count: 5 },
        { emoji: '😆', count: 3 },
        { emoji: '😮', count: 2 },
        { emoji: '😢', count: 1 },
        { emoji: '😡', count: 0 },
      ],
      shareCount: 5,
      commentCount: 3,
      tags: ["tag1", "tag2", "tag3"]
    },
    {
      profilePicture: 'https://images.pexels.com/photos/5393594/pexels-photo-5393594.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500',
      name: 'Alex Johnson',
      date: 'June 21, 2023',
      text: 'Just finished reading an amazing book. Highly recommend it!',
      reactions: [
        { emoji: '👍', count: 10 },
        { emoji: '❤️', count: 5 },
        { emoji: '😆', count: 3 },
        { emoji: '😮', count: 2 },
        { emoji: '😢', count: 1 },
        { emoji: '😡', count: 0 },
      ],
      shareCount: 3,
      commentCount: 2,
      tags: ["tag1", "tag2", "tag3"]
    },
  ];

  const photo = 'https://example.com/profile-photo.jpg';
  const username = 'John Doe';
  const email = 'johndoe@example.com';
  const bio = 'A passionate developer and nature lover.';

  return (
    <div className="main-page">
      <Header />
      <div className="main-content">
        <TagsSection />
        <ContentSection posts={samplePosts} photo={photo} username={username} email={email} bio={bio} />
        <FriendListSection />
      </div>
    </div>
  );
};
export default Profile;

