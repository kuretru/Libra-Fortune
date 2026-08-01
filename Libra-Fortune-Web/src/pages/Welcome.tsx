import { PageContainer } from '@ant-design/pro-components';
import XMarkdown from '@ant-design/x-markdown';
import '@ant-design/x-markdown/es/XMarkdown/index.css';
import { useModel } from '@umijs/max';
import { Card } from 'antd';
import hljs from 'highlight.js';
import React from 'react';

import 'highlight.js/styles/github.css';
import './Welcome.css';
import './Welcome-dark.css';

const readme = __PROJECT_README__;

// XMarkdown Renderer passes class names via non-standard props
interface HeadingProps extends React.HTMLAttributes<HTMLHeadingElement> {
  tag?: string;
  domNode?: unknown;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  classname?: any;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  class?: any;
}

const Heading: React.FC<HeadingProps> = ({
  tag: Tag = 'h1',
  children,
  className,
  classname,
  class: htmlClass,
}) => {
  // Merge all possible class sources from XMarkdown Renderer
  const allClasses = [className, classname, htmlClass]
    .filter(Boolean)
    .join(' ');
  // Extract text content from children for id generation
  const textContent = React.Children.toArray(children)
    .map((child) => (typeof child === 'string' ? child : ''))
    .join('');
  const id = textContent
    .replace(/[^\w\s一-鿿-]/g, '')
    .trim()
    .replace(/\s+/g, '-')
    .toLowerCase();
  const mergedClass = `heading-anchor ${allClasses}`.trim();
  return (
    // @ts-expect-error dynamic tag
    <Tag id={id} className={mergedClass}>
      <a href={`#${id}`} className="anchor-link">
        #
      </a>
      {children}
    </Tag>
  );
};

const mdComponents = {
  h1: (props: HeadingProps) => <Heading tag="h1" {...props} />,
  h2: (props: HeadingProps) => <Heading tag="h2" {...props} />,
  h3: (props: HeadingProps) => <Heading tag="h3" {...props} />,
  h4: (props: HeadingProps) => <Heading tag="h4" {...props} />,
};

const mdConfig = {
  renderer: {
    code({ text, lang }: { text: string; lang?: string }) {
      const langString = (lang || '').trim();
      let highlighted: string;
      if (langString && hljs.getLanguage(langString)) {
        highlighted = hljs.highlight(text.replace(/\n$/, ''), {
          language: langString,
        }).value;
      } else {
        highlighted = hljs.highlightAuto(text.replace(/\n$/, '')).value;
      }
      const classAttr = langString
        ? ` class="hljs language-${langString}"`
        : ' class="hljs"';
      return `<pre><code${classAttr}>${highlighted}\n</code></pre>\n`;
    },
  },
};

const Welcome: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const isDark = initialState?.settings?.navTheme === 'realDark';

  return (
    <PageContainer title="Libra-Fortune">
      <div
        data-theme={isDark ? 'dark' : 'light'}
        className="welcome-markdown"
      >
        <Card>
          <XMarkdown components={mdComponents} config={mdConfig}>
            {readme}
          </XMarkdown>
        </Card>
      </div>
    </PageContainer>
  );
};

export default Welcome;
