import React from 'react';
import './PostPage.css';
import Header from './Header';
import Post from './Post';
import Comments from './Comments';
import CommentInput from './CommentInput';

const PostPage = () => {
  const post = {
    profilePicture: 'profile.jpg',
    name: 'John Doe',
    date: 'June 19, 2023',
    media: 'post-image.jpg',
    text: 'This is the content of the post.',
    reactions: [
      { emoji: '👍', count: 5, isReacted: false },
      { emoji: '❤️', count: 2, isReacted: false },
      { emoji: '😆', count: 1, isReacted: false },
    ],
    shareCount: 10,
    commentCount: 3,
    tags: ['tag1', 'tag2', 'tag3'],
  };

  const comments = [
    {
      profilePicture: 'commenter1.jpg',
      name: 'Jane Smith',
      date: 'June 20, 2023',
      text: 'This is the first comment.',
      reactions: [
        { emoji: '👍', count: 3, isReacted: false },
        { emoji: '❤️', count: 1, isReacted: false },
        { emoji: '😆', count: 0, isReacted: false },
      ],
    },
    {
      profilePicture: 'commenter2.jpg',
      name: 'John Smith',
      date: 'June 21, 2023',
      text: 'This is the second comment.',
      reactions: [
        { emoji: '👍', count: 1, isReacted: false },
        { emoji: '❤️', count: 0, isReacted: false },
        { emoji: '😆', count: 2, isReacted: false },
      ],
    },
  ];

  return (
    <div className="post-page">
      <Header />
      <div className="post-content">
        <Post
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
        <h2>Write your comment on this post</h2>
        <CommentInput/>
        <h2>Comments on this post</h2>
        <Comments comments={comments} />
      </div>
    </div>
  );
};

export default PostPage;
