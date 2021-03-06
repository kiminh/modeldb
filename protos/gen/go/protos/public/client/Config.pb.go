// Code generated by protoc-gen-go. DO NOT EDIT.
// source: protos/public/client/Config.proto

package client

import (
	fmt "fmt"
	proto "github.com/golang/protobuf/proto"
	_ "google.golang.org/genproto/googleapis/api/annotations"
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

// Client config file
// all fields optional
type Config struct {
	Email                string   `protobuf:"bytes,1,opt,name=email,proto3" json:"email,omitempty"`
	DevKey               string   `protobuf:"bytes,2,opt,name=dev_key,json=devKey,proto3" json:"dev_key,omitempty"`
	Host                 string   `protobuf:"bytes,3,opt,name=host,proto3" json:"host,omitempty"`
	Workspace            string   `protobuf:"bytes,4,opt,name=workspace,proto3" json:"workspace,omitempty"`
	Project              string   `protobuf:"bytes,5,opt,name=project,proto3" json:"project,omitempty"`
	Experiment           string   `protobuf:"bytes,6,opt,name=experiment,proto3" json:"experiment,omitempty"`
	Dataset              string   `protobuf:"bytes,7,opt,name=dataset,proto3" json:"dataset,omitempty"`
	XXX_NoUnkeyedLiteral struct{} `json:"-"`
	XXX_unrecognized     []byte   `json:"-"`
	XXX_sizecache        int32    `json:"-"`
}

func (m *Config) Reset()         { *m = Config{} }
func (m *Config) String() string { return proto.CompactTextString(m) }
func (*Config) ProtoMessage()    {}
func (*Config) Descriptor() ([]byte, []int) {
	return fileDescriptor_51f2a5edc2f29c82, []int{0}
}

func (m *Config) XXX_Unmarshal(b []byte) error {
	return xxx_messageInfo_Config.Unmarshal(m, b)
}
func (m *Config) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	return xxx_messageInfo_Config.Marshal(b, m, deterministic)
}
func (m *Config) XXX_Merge(src proto.Message) {
	xxx_messageInfo_Config.Merge(m, src)
}
func (m *Config) XXX_Size() int {
	return xxx_messageInfo_Config.Size(m)
}
func (m *Config) XXX_DiscardUnknown() {
	xxx_messageInfo_Config.DiscardUnknown(m)
}

var xxx_messageInfo_Config proto.InternalMessageInfo

func (m *Config) GetEmail() string {
	if m != nil {
		return m.Email
	}
	return ""
}

func (m *Config) GetDevKey() string {
	if m != nil {
		return m.DevKey
	}
	return ""
}

func (m *Config) GetHost() string {
	if m != nil {
		return m.Host
	}
	return ""
}

func (m *Config) GetWorkspace() string {
	if m != nil {
		return m.Workspace
	}
	return ""
}

func (m *Config) GetProject() string {
	if m != nil {
		return m.Project
	}
	return ""
}

func (m *Config) GetExperiment() string {
	if m != nil {
		return m.Experiment
	}
	return ""
}

func (m *Config) GetDataset() string {
	if m != nil {
		return m.Dataset
	}
	return ""
}

func init() {
	proto.RegisterType((*Config)(nil), "ai.verta.client.Config")
}

func init() {
	proto.RegisterFile("protos/public/client/Config.proto", fileDescriptor_51f2a5edc2f29c82)
}

var fileDescriptor_51f2a5edc2f29c82 = []byte{
	// 255 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0x6c, 0x90, 0x31, 0x4f, 0xc3, 0x30,
	0x10, 0x85, 0x15, 0x68, 0x53, 0xd5, 0x0b, 0x92, 0x85, 0x84, 0x85, 0x2a, 0x04, 0x4c, 0x4c, 0xb9,
	0x81, 0x99, 0xa1, 0x30, 0x21, 0x16, 0xc4, 0xc0, 0xc0, 0x82, 0x9c, 0xe4, 0x70, 0x4d, 0x1d, 0x9f,
	0x65, 0x5f, 0x03, 0xfd, 0x71, 0xfc, 0x37, 0x54, 0xbb, 0x08, 0x06, 0x36, 0xbf, 0xef, 0xf3, 0x1b,
	0xee, 0x89, 0x8b, 0x10, 0x89, 0x29, 0x41, 0xd8, 0xb4, 0xce, 0x76, 0xd0, 0x39, 0x8b, 0x9e, 0xe1,
	0x8e, 0xfc, 0x9b, 0x35, 0x4d, 0x76, 0xf2, 0x48, 0xdb, 0x66, 0xc4, 0xc8, 0xba, 0x29, 0xf6, 0x74,
	0x61, 0x88, 0x8c, 0x43, 0xd0, 0xc1, 0x82, 0xf6, 0x9e, 0x58, 0xb3, 0x25, 0x9f, 0xca, 0xf7, 0xcb,
	0xaf, 0x4a, 0xd4, 0xa5, 0x2f, 0x8f, 0xc5, 0x14, 0x07, 0x6d, 0x9d, 0xaa, 0xce, 0xab, 0xab, 0xf9,
	0x53, 0x09, 0xf2, 0x44, 0xcc, 0x7a, 0x1c, 0x5f, 0xd7, 0xb8, 0x55, 0x07, 0x99, 0xd7, 0x3d, 0x8e,
	0x0f, 0xb8, 0x95, 0x52, 0x4c, 0x56, 0x94, 0x58, 0x1d, 0x66, 0x9a, 0xdf, 0x72, 0x21, 0xe6, 0x1f,
	0x14, 0xd7, 0x29, 0xe8, 0x0e, 0xd5, 0x24, 0x8b, 0x5f, 0x20, 0x95, 0x98, 0x85, 0x48, 0xef, 0xd8,
	0xb1, 0x9a, 0x66, 0xf7, 0x13, 0xe5, 0x99, 0x10, 0xf8, 0x19, 0x30, 0xda, 0x01, 0x3d, 0xab, 0x3a,
	0xcb, 0x3f, 0x64, 0xd7, 0xec, 0x35, 0xeb, 0x84, 0xac, 0x66, 0xa5, 0xb9, 0x8f, 0xb7, 0xcb, 0xc7,
	0xea, 0xe5, 0xc6, 0x58, 0x5e, 0x6d, 0xda, 0xa6, 0xa3, 0x01, 0x9e, 0x77, 0xa7, 0x2f, 0xef, 0x61,
	0xa0, 0x1e, 0x5d, 0xdf, 0xc2, 0x7e, 0x30, 0x83, 0x1e, 0x0c, 0xc1, 0x7f, 0xf3, 0xb5, 0x75, 0xa6,
	0xd7, 0xdf, 0x01, 0x00, 0x00, 0xff, 0xff, 0x81, 0x9f, 0x80, 0x98, 0x5d, 0x01, 0x00, 0x00,
}
