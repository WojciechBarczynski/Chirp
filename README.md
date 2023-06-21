# Chirp - Project for Database course at AGH UST

A simple social media platform, written in Scala with graph database system.

<img src="https://user-images.githubusercontent.com/104033489/234817071-b00870ba-83d5-4e75-9cfa-b0ef01bb2e59.png" width="800">

## Authors

- [Bartosz Rzepa](https://github.com/brzep)
- [Mateusz Marczyk](https://github.com/fantomx775)
- [Wojciech Barczyński](https://github.com/WojciechBarczynski)

## Technologies

- Database: Neo4J
- Backend: Scala

## Vision statement

Social media platform based on Twitter. Main functionality is ment to be adding, displaying posts and interactions between them. Users will be able to tag, comment or react to posts. Their posts recommendations will be based on followed tags/users.

## User stories

As a user I want to add posts in order to tell the world sth. </br>
As a user I want to add tags to created posts in order to specify the target group. </br>
As a user I want to react to posts in order to show my attitude to them. </br>
As a user I want to comment on posts in order to start/participate in meaningful discussions. </br>
As a user I want to react to comments in order to show my attitude to them. </br>
As a user I want to follow other users in order to have their posts recommended. </br>
As a user I want to follow tags in order to have suitable posts recommended. </br>
As a user I want to modify profile info such as bio or photo in order to express myself. </br>
As a user I want to share posts in order to show them to my followers. </br>

## Database schema
### Schema
![image](assets/database_schema.png)

### Relations
![image](assets/relations.png)
## Backend schema

### Initial schema

![image](assets/initial_backend_schema.png)

Due to lack of support for scala in Neo4j drivers (there is only one available unofficial driver, that is under development), we changed our schema to:

### Final schema

![image](assets/new_backend_schema.png)

## Recommendation system

Thanks to using graph database, we could easily retrieve most suitable tags and posts.

We rank posts based on:
1. Their invidual reaction engagement score, based on number of rections
![image](assets/recommendation_system_engagement_score.png)
2. User own preferences score:
- Follow users score: posts created and shared by followed users - collaborative filtering
![image](assets/recommedations_follow_score.png)
- Subscribed tags score: posts tagged with subscribed tags - content-based filtering
![image](assets/recommedations_tag_score.png)
3. Post proximity:
![image](assets/path.png)
#### Most used tags query

```scala
MATCH (post:POST)-[r:TAGGED]->(tag:TAG) RETURN tag, count(r) ORDER BY count(r) DESC LIMIT 10;
```

#### Followed users posts

```scala
MATCH (follower:USER {name:userName})-[:FOLLOW]->(followed:USER)-[:CREATED]->(post:POST) RETURN post;
```

#### Subscribed tags posts

```scala
MATCH (user:USER {name:userName})-[:SUBSCRIBE]->(tag:TAG)<-[:TAGGED]-(post:POST) RETURN post;
```

#### Post reactions

```scala
MATCH (user:USER)-[r:REACTED_TO]->(post:POST) WHERE ID(post)=postId RETURN count(r);
```

## Backend endpoints

We created a few endpoints, that allow to:

1. Login user / create account

```scala
@cask.post("/login")
def login(userName: String)
```

2. Get user recommended posts

```scala
  @cask.get("/users/recommendedPosts")
  def recommendedPosts(userName: String)
```

3. Get recommended tags

```scala
  @cask.get("tag/recommended")
  def recommendedTags()
```

4. Create posts with tags

```scala
@cask.post("/post/create")
def createPost(userName: String, postContent: String, tags: String)
```

5. Comment on posts

```scala
@cask.post("/post/comment")
def createComment(userName: String, commentContent: String, tags: String, postId: String)
```

6. React to post

```scala
@cask.post("/post/react")
def react(userName: String, postId: String, reactionType: String)
```

7. Get post comments

```scala
  @cask.get("/post/comments")
  def getComments(postId: String)
```

8. Follow user

```scala
  @cask.post("/users/follow")
  def followUser(followerUserName: String, followedUserName: String)
```

9. Subscribe tag

```scala
  @cask.post("/tag/subscribe")
  def subscribe(userName: String, tagName: String)
```

10. Get followed users

```scala
  @cask.post("/users/followed")
  def followed(userName: String)
```

11. Get user followers

```scala
  @cask.post("/users/followers")
  def followers(userName: String)
```

12. Update user bio

```scala
  @cask.post("/users/updateBio")
  def updateBio(userName: String, bio: String)
```

### Example returns:

1. Recommended posts:
![image](assets/example_endpoint_return_posts.png)

2. Recommended tags:
![image](assets/example_endpoint_return_tags.png)

### Frontend

Unfortunetly, we haven't finish frontend/backend integration. We only have created mock frontend without calls to Scala backend.

Here is quick look how our feed page would look like.

![image](assets/frontend.png)