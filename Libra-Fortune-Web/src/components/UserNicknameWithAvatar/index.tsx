import { Avatar, Space } from 'antd';

interface UserNicknameWithAvatarProps {
  user: API.User.UserVO | API.Ledger.LedgerMemberVO;
  nickname?: string;
  avatar?: string;
}

const UserNicknameWithAvatar: React.FC<UserNicknameWithAvatarProps> = (props) => {
  return (
    <Space>
      <Avatar src={<img src={props.avatar ?? props.user.avatar} alt="ownerAvatar" />} />
      {props.nickname ?? props.user.nickname}
    </Space>
  );
};

export default UserNicknameWithAvatar;
