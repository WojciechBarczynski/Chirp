import './Post.css'
import { Link } from 'react-router-dom';
import React, { useState } from 'react';

const Post = ({ profilePicture, name, date, media, text, reactions, shareCount, commentCount, tags }) => {
  const [reactionCounts, setReactionCounts] = useState(reactions);
  const [isFollowed, setIsFollowed] = useState(false);

  const handleReactionClick = (index) => {
    const updatedCounts = [...reactionCounts];
    const reactionCount = updatedCounts[index].count;
    const isReacted = updatedCounts[index].isReacted;

    if (isReacted) {
      updatedCounts[index].count -= 1;
      updatedCounts[index].isReacted = false;
    } else {
      updatedCounts[index].count += 1;
      updatedCounts[index].isReacted = true;
    }

    setReactionCounts(updatedCounts);
  };

  const handleFollowClick = () => {
    setIsFollowed(!isFollowed);
  };
  

  return (
    <div className="post">
      <div className="header">
        <img src={profilePicture} alt="Profile" className="profile-picture" />
        <div className="name">{name}</div>
        <div
          className={`follow ${isFollowed ? 'followed' : ''}`}
          onClick={handleFollowClick}
        >
          {isFollowed ? 'Followed' : 'Follow'}
        </div>
        <div className="date">{date}</div>
      </div>
      <div className="tags">
        {tags && tags.length > 0 && (
          <>
            {tags.map((tag, index) => (
              <span key={index} className="tag">{tag}</span>
            ))}
          </>
        )}
      </div>
      <div className="content">
        {media && <img src={media} alt="Media" className="media" />}
        <div className="text">{text}</div>
      </div>
      <div className="community">
        {reactionCounts && Array.isArray(reactionCounts) && (
          <div className="reactions">
            {reactionCounts.map((reaction, index) => (
              <button
                key={index}
                className={`reaction-button ${reaction.isReacted ? 'active' : ''}`}
                onClick={() => handleReactionClick(index)}
              >
                {reaction.emoji}
                <span className="reaction-count">{reaction.count}</span>
              </button>
            ))}
          </div>
        )}
        <button className="share-button">
          Share
          <span className="share-count">{shareCount}</span>
        </button>
        <Link to="/post/1" target="_blank">
          <div className="comments">{commentCount} Show comments</div>
        </Link>
      </div>
    </div>
  );
};

export default Post;
