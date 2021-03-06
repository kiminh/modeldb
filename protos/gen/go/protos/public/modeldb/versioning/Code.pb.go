// Code generated by protoc-gen-go. DO NOT EDIT.
// source: protos/public/modeldb/versioning/Code.proto

package versioning

import (
	fmt "fmt"
	proto "github.com/golang/protobuf/proto"
	math "math"
)

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.ProtoPackageIsVersion3 // please upgrade the proto package

type CodeBlob struct {
	// Types that are valid to be assigned to Content:
	//	*CodeBlob_Git
	//	*CodeBlob_Notebook
	Content              isCodeBlob_Content `protobuf_oneof:"content"`
	XXX_NoUnkeyedLiteral struct{}           `json:"-"`
	XXX_unrecognized     []byte             `json:"-"`
	XXX_sizecache        int32              `json:"-"`
}

func (m *CodeBlob) Reset()         { *m = CodeBlob{} }
func (m *CodeBlob) String() string { return proto.CompactTextString(m) }
func (*CodeBlob) ProtoMessage()    {}
func (*CodeBlob) Descriptor() ([]byte, []int) {
	return fileDescriptor_57d8129ea3c2050d, []int{0}
}

func (m *CodeBlob) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_CodeBlob.Unmarshal(m, b)
}
func (m *CodeBlob) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_CodeBlob.Marshal(b, m, deterministic)
}
func (m *CodeBlob) XXX_Merge(src proto.Message) {
	xxx_messageInfo_CodeBlob.Merge(m, src)
}
func (m *CodeBlob) XXX_Size() int {
	return xxx_messageInfo_CodeBlob.Size(m)
}
func (m *CodeBlob) XXX_DiscardUnknown() {
	xxx_messageInfo_CodeBlob.DiscardUnknown(m)
}

var xxx_messageInfo_CodeBlob proto.InternalMessageInfo

type isCodeBlob_Content interface {
	isCodeBlob_Content()
}

type CodeBlob_Git struct {
	Git *GitCodeBlob `protobuf:"bytes,1,opt,name=git,proto3,oneof"`
}

type CodeBlob_Notebook struct {
	Notebook *NotebookCodeBlob `protobuf:"bytes,2,opt,name=notebook,proto3,oneof"`
}

func (*CodeBlob_Git) isCodeBlob_Content() {}

func (*CodeBlob_Notebook) isCodeBlob_Content() {}

func (m *CodeBlob) GetContent() isCodeBlob_Content {
	if m != nil {
		return m.Content
	}
	return nil
}

func (m *CodeBlob) GetGit() *GitCodeBlob {
	if x, ok := m.GetContent().(*CodeBlob_Git); ok {
		return x.Git
	}
	return nil
}

func (m *CodeBlob) GetNotebook() *NotebookCodeBlob {
	if x, ok := m.GetContent().(*CodeBlob_Notebook); ok {
		return x.Notebook
	}
	return nil
}

// XXX_OneofWrappers is for the internal use of the proto package.
func (*CodeBlob) XXX_OneofWrappers() []interface{} {
	return []interface{}{
		(*CodeBlob_Git)(nil),
		(*CodeBlob_Notebook)(nil),
	}
}

type GitCodeBlob struct {
	Repo                 string   `protobuf:"bytes,1,opt,name=repo,proto3" json:"repo,omitempty"`
	Hash                 string   `protobuf:"bytes,2,opt,name=hash,proto3" json:"hash,omitempty"`
	Branch               string   `protobuf:"bytes,3,opt,name=branch,proto3" json:"branch,omitempty"`
	Tag                  string   `protobuf:"bytes,4,opt,name=tag,proto3" json:"tag,omitempty"`
	IsDirty              bool     `protobuf:"varint,5,opt,name=is_dirty,json=isDirty,proto3" json:"is_dirty,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *GitCodeBlob) Reset()         { *m = GitCodeBlob{} }
func (m *GitCodeBlob) String() string { return proto.CompactTextString(m) }
func (*GitCodeBlob) ProtoMessage()    {}
func (*GitCodeBlob) Descriptor() ([]byte, []int) {
	return fileDescriptor_57d8129ea3c2050d, []int{1}
}

func (m *GitCodeBlob) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_GitCodeBlob.Unmarshal(m, b)
}
func (m *GitCodeBlob) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_GitCodeBlob.Marshal(b, m, deterministic)
}
func (m *GitCodeBlob) XXX_Merge(src proto.Message) {
	xxx_messageInfo_GitCodeBlob.Merge(m, src)
}
func (m *GitCodeBlob) XXX_Size() int {
	return xxx_messageInfo_GitCodeBlob.Size(m)
}
func (m *GitCodeBlob) XXX_DiscardUnknown() {
	xxx_messageInfo_GitCodeBlob.DiscardUnknown(m)
}

var xxx_messageInfo_GitCodeBlob proto.InternalMessageInfo

func (m *GitCodeBlob) GetRepo() string {
	if m != nil {
		return m.Repo
	}
	return ""
}

func (m *GitCodeBlob) GetHash() string {
	if m != nil {
		return m.Hash
	}
	return ""
}

func (m *GitCodeBlob) GetBranch() string {
	if m != nil {
		return m.Branch
	}
	return ""
}

func (m *GitCodeBlob) GetTag() string {
	if m != nil {
		return m.Tag
	}
	return ""
}

func (m *GitCodeBlob) GetIsDirty() bool {
	if m != nil {
		return m.IsDirty
	}
	return false
}

type NotebookCodeBlob struct {
	Path                 *PathDatasetComponentBlob `protobuf:"bytes,1,opt,name=path,proto3" json:"path,omitempty"`
	GitRepo              *GitCodeBlob              `protobuf:"bytes,2,opt,name=git_repo,json=gitRepo,proto3" json:"git_repo,omitempty"`
	XXX_NoUnkeyedLiteral struct{}                  `json:"-"`
	XXX_unrecognized     []byte                    `json:"-"`
	XXX_sizecache        int32                     `json:"-"`
}

func (m *NotebookCodeBlob) Reset()         { *m = NotebookCodeBlob{} }
func (m *NotebookCodeBlob) String() string { return proto.CompactTextString(m) }
func (*NotebookCodeBlob) ProtoMessage()    {}
func (*NotebookCodeBlob) Descriptor() ([]byte, []int) {
	return fileDescriptor_57d8129ea3c2050d, []int{2}
}

func (m *NotebookCodeBlob) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_NotebookCodeBlob.Unmarshal(m, b)
}
func (m *NotebookCodeBlob) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_NotebookCodeBlob.Marshal(b, m, deterministic)
}
func (m *NotebookCodeBlob) XXX_Merge(src proto.Message) {
	xxx_messageInfo_NotebookCodeBlob.Merge(m, src)
}
func (m *NotebookCodeBlob) XXX_Size() int {
	return xxx_messageInfo_NotebookCodeBlob.Size(m)
}
func (m *NotebookCodeBlob) XXX_DiscardUnknown() {
	xxx_messageInfo_NotebookCodeBlob.DiscardUnknown(m)
}

var xxx_messageInfo_NotebookCodeBlob proto.InternalMessageInfo

func (m *NotebookCodeBlob) GetPath() *PathDatasetComponentBlob {
	if m != nil {
		return m.Path
	}
	return nil
}

func (m *NotebookCodeBlob) GetGitRepo() *GitCodeBlob {
	if m != nil {
		return m.GitRepo
	}
	return nil
}

type CodeDiff struct {
	// Types that are valid to be assigned to Content:
	//	*CodeDiff_Git
	//	*CodeDiff_Notebook
	Content              isCodeDiff_Content `protobuf_oneof:"content"`
	XXX_NoUnkeyedLiteral struct{}           `json:"-"`
	XXX_unrecognized     []byte             `json:"-"`
	XXX_sizecache        int32              `json:"-"`
}

func (m *CodeDiff) Reset()         { *m = CodeDiff{} }
func (m *CodeDiff) String() string { return proto.CompactTextString(m) }
func (*CodeDiff) ProtoMessage()    {}
func (*CodeDiff) Descriptor() ([]byte, []int) {
	return fileDescriptor_57d8129ea3c2050d, []int{3}
}

func (m *CodeDiff) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_CodeDiff.Unmarshal(m, b)
}
func (m *CodeDiff) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_CodeDiff.Marshal(b, m, deterministic)
}
func (m *CodeDiff) XXX_Merge(src proto.Message) {
	xxx_messageInfo_CodeDiff.Merge(m, src)
}
func (m *CodeDiff) XXX_Size() int {
	return xxx_messageInfo_CodeDiff.Size(m)
}
func (m *CodeDiff) XXX_DiscardUnknown() {
	xxx_messageInfo_CodeDiff.DiscardUnknown(m)
}

var xxx_messageInfo_CodeDiff proto.InternalMessageInfo

type isCodeDiff_Content interface {
	isCodeDiff_Content()
}

type CodeDiff_Git struct {
	Git *GitCodeDiff `protobuf:"bytes,1,opt,name=git,proto3,oneof"`
}

type CodeDiff_Notebook struct {
	Notebook *NotebookCodeDiff `protobuf:"bytes,2,opt,name=notebook,proto3,oneof"`
}

func (*CodeDiff_Git) isCodeDiff_Content() {}

func (*CodeDiff_Notebook) isCodeDiff_Content() {}

func (m *CodeDiff) GetContent() isCodeDiff_Content {
	if m != nil {
		return m.Content
	}
	return nil
}

func (m *CodeDiff) GetGit() *GitCodeDiff {
	if x, ok := m.GetContent().(*CodeDiff_Git); ok {
		return x.Git
	}
	return nil
}

func (m *CodeDiff) GetNotebook() *NotebookCodeDiff {
	if x, ok := m.GetContent().(*CodeDiff_Notebook); ok {
		return x.Notebook
	}
	return nil
}

// XXX_OneofWrappers is for the internal use of the proto package.
func (*CodeDiff) XXX_OneofWrappers() []interface{} {
	return []interface{}{
		(*CodeDiff_Git)(nil),
		(*CodeDiff_Notebook)(nil),
	}
}

type GitCodeDiff struct {
	Status               DiffStatusEnum_DiffStatus `protobuf:"varint,1,opt,name=status,proto3,enum=ai.verta.modeldb.versioning.DiffStatusEnum_DiffStatus" json:"status,omitempty"`
	A                    *GitCodeBlob              `protobuf:"bytes,2,opt,name=A,proto3" json:"A,omitempty"`
	B                    *GitCodeBlob              `protobuf:"bytes,3,opt,name=B,proto3" json:"B,omitempty"`
	C                    *GitCodeBlob              `protobuf:"bytes,4,opt,name=C,proto3" json:"C,omitempty"`
	XXX_NoUnkeyedLiteral struct{}                  `json:"-"`
	XXX_unrecognized     []byte                    `json:"-"`
	XXX_sizecache        int32                     `json:"-"`
}

func (m *GitCodeDiff) Reset()         { *m = GitCodeDiff{} }
func (m *GitCodeDiff) String() string { return proto.CompactTextString(m) }
func (*GitCodeDiff) ProtoMessage()    {}
func (*GitCodeDiff) Descriptor() ([]byte, []int) {
	return fileDescriptor_57d8129ea3c2050d, []int{4}
}

func (m *GitCodeDiff) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_GitCodeDiff.Unmarshal(m, b)
}
func (m *GitCodeDiff) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_GitCodeDiff.Marshal(b, m, deterministic)
}
func (m *GitCodeDiff) XXX_Merge(src proto.Message) {
	xxx_messageInfo_GitCodeDiff.Merge(m, src)
}
func (m *GitCodeDiff) XXX_Size() int {
	return xxx_messageInfo_GitCodeDiff.Size(m)
}
func (m *GitCodeDiff) XXX_DiscardUnknown() {
	xxx_messageInfo_GitCodeDiff.DiscardUnknown(m)
}

var xxx_messageInfo_GitCodeDiff proto.InternalMessageInfo

func (m *GitCodeDiff) GetStatus() DiffStatusEnum_DiffStatus {
	if m != nil {
		return m.Status
	}
	return DiffStatusEnum_UNKNOWN
}

func (m *GitCodeDiff) GetA() *GitCodeBlob {
	if m != nil {
		return m.A
	}
	return nil
}

func (m *GitCodeDiff) GetB() *GitCodeBlob {
	if m != nil {
		return m.B
	}
	return nil
}

func (m *GitCodeDiff) GetC() *GitCodeBlob {
	if m != nil {
		return m.C
	}
	return nil
}

type NotebookCodeDiff struct {
	Path                 *PathDatasetComponentDiff `protobuf:"bytes,1,opt,name=path,proto3" json:"path,omitempty"`
	GitRepo              *GitCodeDiff              `protobuf:"bytes,2,opt,name=git_repo,json=gitRepo,proto3" json:"git_repo,omitempty"`
	XXX_NoUnkeyedLiteral struct{}                  `json:"-"`
	XXX_unrecognized     []byte                    `json:"-"`
	XXX_sizecache        int32                     `json:"-"`
}

func (m *NotebookCodeDiff) Reset()         { *m = NotebookCodeDiff{} }
func (m *NotebookCodeDiff) String() string { return proto.CompactTextString(m) }
func (*NotebookCodeDiff) ProtoMessage()    {}
func (*NotebookCodeDiff) Descriptor() ([]byte, []int) {
	return fileDescriptor_57d8129ea3c2050d, []int{5}
}

func (m *NotebookCodeDiff) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_NotebookCodeDiff.Unmarshal(m, b)
}
func (m *NotebookCodeDiff) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_NotebookCodeDiff.Marshal(b, m, deterministic)
}
func (m *NotebookCodeDiff) XXX_Merge(src proto.Message) {
	xxx_messageInfo_NotebookCodeDiff.Merge(m, src)
}
func (m *NotebookCodeDiff) XXX_Size() int {
	return xxx_messageInfo_NotebookCodeDiff.Size(m)
}
func (m *NotebookCodeDiff) XXX_DiscardUnknown() {
	xxx_messageInfo_NotebookCodeDiff.DiscardUnknown(m)
}

var xxx_messageInfo_NotebookCodeDiff proto.InternalMessageInfo

func (m *NotebookCodeDiff) GetPath() *PathDatasetComponentDiff {
	if m != nil {
		return m.Path
	}
	return nil
}

func (m *NotebookCodeDiff) GetGitRepo() *GitCodeDiff {
	if m != nil {
		return m.GitRepo
	}
	return nil
}

func init() {
	proto.RegisterType((*CodeBlob)(nil), "ai.verta.modeldb.versioning.CodeBlob")
	proto.RegisterType((*GitCodeBlob)(nil), "ai.verta.modeldb.versioning.GitCodeBlob")
	proto.RegisterType((*NotebookCodeBlob)(nil), "ai.verta.modeldb.versioning.NotebookCodeBlob")
	proto.RegisterType((*CodeDiff)(nil), "ai.verta.modeldb.versioning.CodeDiff")
	proto.RegisterType((*GitCodeDiff)(nil), "ai.verta.modeldb.versioning.GitCodeDiff")
	proto.RegisterType((*NotebookCodeDiff)(nil), "ai.verta.modeldb.versioning.NotebookCodeDiff")
}

func init() {
	proto.RegisterFile("protos/public/modeldb/versioning/Code.proto", fileDescriptor_57d8129ea3c2050d)
}

var fileDescriptor_57d8129ea3c2050d = []byte{
	// 447 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0xac, 0x54, 0x41, 0x8b, 0x13, 0x31,
	0x18, 0x35, 0x6d, 0x6d, 0xa7, 0x59, 0x90, 0x25, 0x07, 0x19, 0xd7, 0xcb, 0xd2, 0x53, 0x41, 0xcd,
	0xc0, 0x8a, 0x7b, 0xf2, 0xd2, 0x99, 0x8a, 0x16, 0x71, 0x59, 0x22, 0x78, 0xf0, 0xb2, 0x64, 0xda,
	0x6c, 0x26, 0xd8, 0x26, 0xc3, 0xe4, 0xeb, 0x82, 0xde, 0xfd, 0x1f, 0xe2, 0x2f, 0x95, 0x7c, 0x9b,
	0xed, 0x16, 0x95, 0x96, 0xb1, 0xde, 0x92, 0xf0, 0xde, 0xcb, 0xcb, 0xf7, 0x1e, 0xa1, 0xcf, 0xea,
	0xc6, 0x81, 0xf3, 0x59, 0xbd, 0x2e, 0x97, 0x66, 0x9e, 0xad, 0xdc, 0x42, 0x2d, 0x17, 0x65, 0x76,
	0xa3, 0x1a, 0x6f, 0x9c, 0x35, 0x56, 0x67, 0x85, 0x5b, 0x28, 0x8e, 0x28, 0xf6, 0x54, 0x1a, 0x7e,
	0xa3, 0x1a, 0x90, 0x3c, 0xe2, 0xf8, 0x3d, 0xee, 0x84, 0xef, 0x55, 0x9a, 0x4a, 0x90, 0x5e, 0xc1,
	0x2d, 0xf0, 0xe4, 0xf9, 0x5e, 0xfc, 0x1b, 0xbb, 0x5e, 0xf9, 0x5b, 0xf4, 0xe8, 0x07, 0xa1, 0x49,
	0x70, 0x92, 0x2f, 0x5d, 0xc9, 0x5e, 0xd3, 0xae, 0x36, 0x90, 0x92, 0x53, 0x32, 0x3e, 0x3a, 0x1b,
	0xf3, 0x1d, 0xae, 0xf8, 0x5b, 0x03, 0x77, 0xb4, 0x77, 0x0f, 0x44, 0xa0, 0xb1, 0xf7, 0x34, 0xb1,
	0x0e, 0x54, 0xe9, 0xdc, 0x97, 0xb4, 0x83, 0x12, 0x2f, 0x76, 0x4a, 0x5c, 0x44, 0xf0, 0x96, 0xce,
	0x46, 0x20, 0x1f, 0xd2, 0xc1, 0xdc, 0x59, 0x50, 0x16, 0x46, 0xdf, 0xe8, 0xd1, 0xd6, 0x6d, 0x8c,
	0xd1, 0x5e, 0xa3, 0x6a, 0x87, 0x2e, 0x87, 0x02, 0xd7, 0xe1, 0xac, 0x92, 0xbe, 0xc2, 0x6b, 0x87,
	0x02, 0xd7, 0xec, 0x31, 0xed, 0x97, 0x8d, 0xb4, 0xf3, 0x2a, 0xed, 0xe2, 0x69, 0xdc, 0xb1, 0x63,
	0xda, 0x05, 0xa9, 0xd3, 0x1e, 0x1e, 0x86, 0x25, 0x7b, 0x42, 0x13, 0xe3, 0xaf, 0x16, 0xa6, 0x81,
	0xaf, 0xe9, 0xc3, 0x53, 0x32, 0x4e, 0xc4, 0xc0, 0xf8, 0x69, 0xd8, 0x8e, 0x7e, 0x12, 0x7a, 0xfc,
	0xbb, 0x4f, 0x36, 0xa3, 0xbd, 0x5a, 0x42, 0x15, 0xe7, 0xf4, 0x6a, 0xe7, 0x23, 0x2f, 0x25, 0x54,
	0x31, 0x9f, 0xc2, 0xad, 0x6a, 0x67, 0x95, 0x85, 0x20, 0x22, 0x50, 0x82, 0x15, 0x34, 0xd1, 0x06,
	0xae, 0xf0, 0x41, 0x9d, 0x76, 0x63, 0x17, 0x03, 0x6d, 0x40, 0xa8, 0xfa, 0x3e, 0xc3, 0xa9, 0xb9,
	0xbe, 0xfe, 0x87, 0x0c, 0x03, 0xed, 0x7f, 0x64, 0x18, 0x75, 0xfe, 0x9a, 0xe1, 0xf7, 0xce, 0x26,
	0x44, 0x74, 0x79, 0x41, 0xfb, 0x1e, 0x24, 0xac, 0x3d, 0x1a, 0x7d, 0x74, 0x76, 0xbe, 0xf3, 0x96,
	0x40, 0xf9, 0x88, 0xf0, 0x50, 0xdd, 0xad, 0xad, 0x88, 0x2a, 0xec, 0x9c, 0x92, 0x49, 0xeb, 0x01,
	0x92, 0x49, 0xe0, 0xe5, 0xd8, 0x8f, 0x56, 0xbc, 0x3c, 0xf0, 0x0a, 0xac, 0x50, 0x2b, 0x5e, 0xf1,
	0x47, 0x9f, 0x70, 0x18, 0x87, 0xf6, 0x29, 0x88, 0x1c, 0xd6, 0x27, 0x54, 0xb8, 0xeb, 0x53, 0xfe,
	0xe1, 0x92, 0x7c, 0x9e, 0x69, 0x03, 0xd5, 0xba, 0xe4, 0x73, 0xb7, 0xca, 0x3e, 0x05, 0xf6, 0x64,
	0xb6, 0xf9, 0x4a, 0xe2, 0x07, 0xa3, 0x95, 0xcd, 0xb4, 0xcb, 0xf6, 0x7d, 0x37, 0x65, 0x1f, 0x11,
	0x2f, 0x7f, 0x05, 0x00, 0x00, 0xff, 0xff, 0x3c, 0x45, 0xde, 0xae, 0x13, 0x05, 0x00, 0x00,
}
