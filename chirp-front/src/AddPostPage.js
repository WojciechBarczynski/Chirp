import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './AddPostPage.css';

const AddPostPage = () => {
  const [media, setMedia] = useState('');
  const [selectedTag, setSelectedTag] = useState('');
  const [newTag, setNewTag] = useState('');
  const [tags, setTags] = useState(['tag1', 'tag2', 'tag3']);
  const [text, setText] = useState('');

  const handleMediaChange = (e) => {
    setMedia(e.target.value);
  };

  const handleTagChange = (e) => {
    setSelectedTag(e.target.value);
  };

  const handleNewTagChange = (e) => {
    setNewTag(e.target.value);
  };

  const handleTagAdd = () => {
    if (newTag.trim() !== '') {
      const isExistingTag = tags.some(tag => tag.toLowerCase() === newTag.toLowerCase());
      if (isExistingTag) {
        alert('Tag already exists!');
      } else {
        setTags([...tags, newTag]);
      }
      setSelectedTag(newTag);
      setNewTag('');
    }
  };
  
  

  const handleTagRemove = (tag) => {
    const updatedTags = tags.filter((t) => t !== tag);
    setTags(updatedTags);
    if (selectedTag === tag) {
      setSelectedTag('');
    }
  };

  const handleTextChange = (e) => {
    setText(e.target.value);
  };

  const handleCancel = () => {
    // Handle cancel logic
    console.log('Adding new post was cancelled');
    // Reset form fields
    setMedia('');
    setSelectedTag('');
    setNewTag('');
    setText('');
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic
    console.log('Submitted:', { media, selectedTag, text });
    // Reset form fields
    setMedia('');
    setSelectedTag('');
    setNewTag('');
    setText('');
  };

  return (
    <div className="add-post-page">
      <h1>Add Post</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group" add-media>
          <label htmlFor="media">Add Media</label>
          <input
            type="file"
            id="media"
            value={media}
            onChange={handleMediaChange}
          />
        </div>
        <div className="form-group tags">
          <label htmlFor="tags">Tags</label>
          {/* <select
            id="tags"
            value={selectedTag}
            onChange={handleTagChange}
          >
            <option value="">Select a tag</option>
            {tags.map((tag, index) => (
              <option key={index} value={tag}>{tag}</option>
            ))}
          </select> */}
          <input
            type="text"
            value={newTag}
            onChange={handleNewTagChange}
            placeholder="Enter tag"
          />
          <button type="button" onClick={handleTagAdd}>
            Add Tag
          </button>
        </div>
        <div className="tags">
          {tags.map((tag, index) => (
            <div key={index} className="tag">
              {tag}
              <button
                type="button"
                className="remove-tag"
                onClick={() => handleTagRemove(tag)}
              >
                X
              </button>
            </div>
          ))}
        </div>
        <div className="form-group text-section">
          <label htmlFor="text">Text</label>
          <textarea
            id="text"
            value={text}
            onChange={handleTextChange}
            rows={4}
            placeholder="Enter text"
          />
        </div>
        <div className="form-buttons">
          <Link to="/">
            <button type="button" onClick={handleCancel}>Cancel</button>
          </Link>
          <button type="submit">Submit</button>
        </div>
      </form>
    </div>
  );
};

export default AddPostPage;
