# Chirp - Project for Database course at AGH UST

A simple social media platform, written in Scala with graph database system.


## Authors:
- [Bartosz Rzepa](https://github.com/brzep)
- [Mateusz Marczyk](https://github.com/fantomx775)
- [Wojciech Barczyński](https://github.com/WojciechBarczynski)

## Technologies:
- Database: Neo4J
- Backend: Scala

## Vision statement
Social media platform based on Twitter. Main functionality is ment to be adding, displaying posts and interactions between them. Users will be able to tag, comment or react to posts. Their posts recommendations will be based on followed tags/users.

## User stories
As a user I want to add posts in order to tell the world sth.
As a user I want to add photos to post in order to make them more diverse.
As a user I want to add tags to created posts in order to specify the target group.
As a user I want to react to posts in order to show my attitude to them.
As a user I want to comment on posts in order to start/participate in meaningful discussions.
As a user I want to react to comments in order to show my attitude to them.
As a user I want to follow other users in order to have their posts recommended.
As a user I want to follow tags in order to have suitable posts recommended.
As a user I want to specify the level of following to make some criteria more important than others.
As a user I want to block other users/tags in order not to have their content recommended.
As a user I want to modify profile info such as bio or photo in order to express myself.
As a user I want to share posts in order to show them to my followers.


## Database diagram
![image](https://user-images.githubusercontent.com/63919870/234395692-d3da90df-de68-4ceb-b1fc-ddc0af2fdab6.png)

## Data model
Nodes and relationships can be mapped by Scala's case classes.

### Nodes model
1. User node:
```scala
enum UserStatus: 
  case Active, Blocked, Deleted

case class User(email: String, username: String, password_hash: String, photo: String, bio: String, status: UserStatus)
```

2. Post node:
```scala
enum ResourceKind:
  case Photo, Link

case class Resource(kind: ResourceKind, url: String)

case class Post(content: String, resources: List[Resource])
```

3. Tag node:
```scala
case class Tag(name: String)
```

### Relationships model
1. Follows | User -> User relationship
```scala
enum FollowLevel:
  case AllPosts, PopularPosts, RandomPosts, Blocked

case class Follows(level: FollowLevel)
```

2. ReactedTo | User -> Post relationship
```scala
enum ReactionType:
  case Like, Love, Haha, Wow, Sad, Angry

case class ReactedTo(reaction_type: ReactionType)
```

3. Shared | User -> Post relationship
```scala
case class Shared(datetime: DateTime)
```

4. CreatedBy | Post -> User relationship
```scala
case class CreatedBy(datetime: DateTime)
```

5. Subscribes | User -> Tag relationship
```scala
case class Subscribes(level: FollowLevel)
```

6. CommentedOn | Post -> Post relationship
```scala
case class CommentedOn()
```

7. Tagged | Post -> Tag relationship
```scala
case class Tagged()
```

## Cypher commends
### Addind nodes
1. Add user
```scala
CREATE (user:USER {
    email: "email@gmail.com", 
    username: "JanKowlaski", 
    password_hash: "SOME_PASSWORD_HASH", 
    photo: "SOME_PHOTO_URL", 
    bio: "SOME_BIO", 
    status: "SOME_USER_STATUS"
})
```
2. Add post
```scala
CREATE (post:POST {
    content: "SOME_CONTENT",
    resources: ["resource_url_0", "resource_url_1"]
})
```
3. Add tag
```scala
CREATE (tag:TAG {name: "SOME_TAG_NAME"})
```

### Adding relationships
1. Add `FOLLOW` relationship. </br>
User with email: `"email0"` follows user with email: `"email1"` with `level` `"level"`.
```scala
MATCH (u0:USER {email: "email0"}), (u1:USER {email: "email1"}) 
CREATE (u0)-[r:FOLLOWS {level: "level"}]->(u1)
```

2. Add `REACTED_TO` relationship. </br>
User `user` reacts to post `post` with reaction type `type`.
```scala
CREATE (user)-[r:REACTED_TO {reaction_type: "type"}]->(post)
```

3. Add `SHARED` relationship. </br>
User `user` share post `post` on `datetime`.
```scala
CREATE (user)-[r:SHARED {datetime: datetime("datetime")}]-(post)
```

4. Add `CREATED_BY` relationship. </br>
Post `post` is created by user `user` on `datetime`.
```scala
CREATE (post)-[r:CREATED_BY {datetime: datetime("datetime")}]-(user)
```

5. Add `SUBSCRIBES` relationship. </br>
User `user` subscribes tag `tag` with level `"level"`.
```scala
CREATE (user)-[r:SUBSCRIBES {level: "level"}]-(tag)
```

6. Add `COMMENTED_ON` relationship. </br>
Post `comment_post` comments on post `post`.
```scala
CREATE (comment_post)-[r:COMMENTED_ON]-(post)
```

7. Add `TAGGED` relationship. </br>
Post `post` gets tagged with tag `tag`.
```scala
CREATE (post)-[r:TAGGED]-(tag)
```

### Constraints
#### Unique nodes values constraints
1. User unique_email 
```scala
CREATE CONSTRAINT unique_email 
FOR (user:USER) REQUIRE user.email IS UNIQUE
```

2. User unique_username
```scala
CREATE CONSTRAINT unique_username 
FOR (user:USER) REQUIRE user.username IS UNIQUE
```

3. Tag unique_name
```scala
CREATE CONSTRAINT unique_tag_name 
FOR (tag:TAG) REQUIRE tag.name IS UNIQUE
```
